package ru.voskhod.smev.client.api.signature.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import ru.voskhod.crypto.DigitalSignatureFactory;
import ru.voskhod.crypto.KeyStoreWrapper;
import ru.voskhod.crypto.exceptions.SignatureProcessingException;
import ru.voskhod.crypto.exceptions.SignatureValidationException;
import ru.voskhod.smev.client.api.services.signature.Signer;
import ru.voskhod.smev.client.api.services.signature.configuration.SignerConfiguration;
import ru.voskhod.smev.client.api.types.exception.SMEVException;
import ru.voskhod.smev.client.api.types.exception.SMEVRuntimeException;
import ru.voskhod.smev.client.api.types.exception.processing.SMEVSignatureException;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;


/**
 * The type Signer.
 */
public class SignerImpl implements Signer {

    private static volatile boolean init = false;

    private final SMEVCertificateStore certificateStore;
    private PrivateKey privateKey;
    private X509Certificate certificate;

    /**
     * Instantiates a new Signer.
     *
     * @param signConfig the sign config
     */
    public SignerImpl(SignerConfiguration signConfig) {
        init(signConfig);
        if (signConfig.getSMEVFileCertificateStore() != null) {
            certificateStore = new SMEVCertificateStore(signConfig.getSMEVFileCertificateStore());
        } else {
            certificateStore = null;
        }
    }

    private static synchronized void init(SignerConfiguration signConfig) {
        if (!init) {
            synchronized (SignerImpl.class) {
                if (!init) {
                    DigitalSignatureFactory.init(signConfig.getProviderName());
                    init = true;
                }
            }
        }
    }

    /**
     * Загружает приватный ключ и сертификат из хранилища и возвращает их
     *
     * @return пару: приватный ключ и сертификат
     */
    @Override
    public PrivateKey getKey(String alias, String password) throws SMEVRuntimeException {
        KeyStoreWrapper keyStore = DigitalSignatureFactory.getKeyStoreWrapper();
        try {
            return keyStore.getPrivateKey(alias, password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SMEVRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Загружает приватный ключ и сертификат из хранилища и возвращает их
     *
     * @return пару: приватный ключ и сертификат
     */
    @Override
    public X509Certificate getCertificate(String alias) throws SMEVRuntimeException {
        KeyStoreWrapper keyStore = DigitalSignatureFactory.getKeyStoreWrapper();
        try {
            return keyStore.getX509Certificate(alias);
        } catch (CertificateException | KeyStoreException e) {
            throw new SMEVRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void init(String cert, String privateKey, String pass) throws SMEVRuntimeException {
        init(getKey(privateKey, pass), getCertificate(cert));
    }

    @Override
    public void init(PrivateKey privateKey, X509Certificate certificate) throws SMEVRuntimeException {
        if (this.privateKey == null && this.certificate == null) {
            this.privateKey = privateKey;
            this.certificate = certificate;
        } else {
            throw new SMEVRuntimeException("Already initialized!");
        }
    }

    @Override
    public Element sign(Element content2sign) throws SMEVException {
        try {
            return signXMLDSigDetached(content2sign, null);
        } catch (DOMException ex) {
            throw new SMEVRuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public Element signXMLDSigDetached(Element document2Sign, String signatureId) throws SMEVSignatureException, SMEVRuntimeException {
        check();
        try {
            return DigitalSignatureFactory.getDigitalSignatureProcessor().signXMLDSigDetached(document2Sign, signatureId, privateKey, certificate);
        } catch (SignatureProcessingException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] signPKCS7Detached(byte[] digest) throws SMEVSignatureException, SMEVRuntimeException {
        check();
        try {
            return DigitalSignatureFactory.getDigitalSignatureProcessor().signPKCS7Detached(digest, privateKey, certificate);
        } catch (SignatureProcessingException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }

    private void check() throws SMEVRuntimeException {
        if (this.privateKey == null || this.certificate == null) {
            throw new SMEVRuntimeException("Not initialized!");
        }
    }

    @Override
    public void validateSMEVSignature(Element smevSignature, Element content2validate) throws SMEVSignatureException {
        try {
            if (smevSignature == null) {
                throw new SignatureProcessingException("Signature is missing");
            }
            X509Certificate smevCertificate = DigitalSignatureFactory.getDigitalSignatureProcessor().validateXMLDSigDetachedSignature(content2validate, smevSignature);

            //TODO: checkExpired(smevCertificate);

            //TODO:  checkIdentity(smevCertificate);
        } catch (SignatureProcessingException | SignatureValidationException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }

    private void checkExpired(X509Certificate certificate) throws SignatureProcessingException {
        // Проверим сертификат СМЭВ на срок действия.
        try {
            certificate.checkValidity();
        } catch (CertificateExpiredException e) {
            throw new SignatureProcessingException("SMEV certificate is expired", e);
        } catch (CertificateNotYetValidException e) {
            throw new SignatureProcessingException("SMEV certificate is not yet valid", e);
        }
    }

    private void checkIdentity(X509Certificate certificate) throws SignatureValidationException, SignatureProcessingException {
        // Проверим сертификат СМЭВ на соответствие тому, который хранится локально.
        try {
            if (certificateStore != null && !certificateStore.isKnown(certificate)) {
                throw new SignatureValidationException("SMEV certificate obtained from signature is not identified as one belonging to SMEV");
            }
        } catch (CertificateException | IOException e) {
            throw new SignatureProcessingException("SMEV certificate store malfunction", e);
        }
    }

    @Override
    public byte[] getDigest(File file) throws SMEVSignatureException {
        try (DigestInputStream inputStream = getDigestInputStream(file)) {
            return getDigest(inputStream);
        } catch (IOException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getDigest(DigestInputStream digestStream) throws SMEVSignatureException {
        try {
            while (true) {
                int c = digestStream.read();
                if (c < 0)
                    break;
            }
            return digestStream.getMessageDigest().digest();
        } catch (IOException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }

    @Override
    public DigestInputStream getDigestInputStream(File file) throws SMEVSignatureException {
        try {
            return getDigestInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }

    @Override
    public DigestInputStream getDigestInputStream(InputStream inputStream) throws SMEVSignatureException {
        try {
            return new DigestInputStream(inputStream, DigitalSignatureFactory.getDigitalSignatureProcessor().getDigest());
        } catch (SignatureProcessingException e) {
            throw new SMEVSignatureException(e.getMessage(), e);
        }
    }
}

