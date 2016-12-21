package ru.voskhod.smev.client.api.signature.impl;

import ru.voskhod.smev.client.api.services.signature.Signer;
import ru.voskhod.smev.client.api.services.signature.configuration.SignerConfiguration;
import ru.voskhod.smev.client.api.types.exception.SMEVRuntimeException;

/**
 * The type Signer factory.
 */
public class SignerFactory {
    private SignerFactory() {
    }

    /**
     * Gets signer.
     *
     * @param signConfig the sign config
     * @param cert       the cert
     * @param privateKey the private key
     * @param pass       the pass
     * @return the signer
     * @throws SMEVRuntimeException the smev runtime exception
     */
    public static Signer getSigner(SignerConfiguration signConfig, String cert, String privateKey, String pass) throws SMEVRuntimeException {
        Signer signer = new SignerImpl(signConfig);
        signer.init(cert, privateKey, pass);
        return signer;
    }
}
