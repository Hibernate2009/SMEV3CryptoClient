package ru.voskhod.smev.client.api.signature.impl;

import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Smev certificate store.
 */
class SMEVCertificateStore {

    private static final long CACHE_UPDATE_INTERVAL_MILLIS = 1000;

    private final List<X509Certificate> certificateCache = new ArrayList<>();
    private final List<X509Certificate> negativeCertificateCache = new ArrayList<>();
    private final File directory;
    private long lastCacheUpdate = 0;

    /**
     * Instantiates a new Smev certificate store.
     *
     * @param directory the directory
     */
    public SMEVCertificateStore(File directory) {
        this.directory = directory;
    }

    /**
     * Gets smev certificates.
     *
     * @return the smev certificates
     * @throws IOException          the io exception
     * @throws CertificateException the certificate exception
     */
    protected List<X509Certificate> getSMEVCertificates() throws IOException, CertificateException {
        List<X509Certificate> result = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files != null) {
            for (File item : files) {
                if (item.isDirectory()) {
                    continue;
                }
                try (InputStream in = new FileInputStream(item)) {
                    BufferedInputStream b64in = new BufferedInputStream(in);
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate) cf.generateCertificate(b64in);
                    result.add(cert);
                }
            }
        }
        return result;
    }

    /**
     * Известен ли такой сертификат СМЭВ.
     *
     * @param certificate сертификат, предположительно принадлежащий СМЭВ.
     * @return true, если такой сертификат известен как принадлежащий СМЭВ.
     * @throws IOException          the io exception
     * @throws CertificateException the certificate exception
     */
    public boolean isKnown(X509Certificate certificate) throws IOException, CertificateException {
        // Может случиться так, что по ходу работы приложения в хранилище добавили сертификат.
        // Поэтому при отсутствии его в кэше пытаемся обновить кэш.
        synchronized (this) {
            if (certificateCache.contains(certificate))
                return true;
            long now = System.currentTimeMillis();
            if (negativeCertificateCache.contains(certificate) && now - lastCacheUpdate <= CACHE_UPDATE_INTERVAL_MILLIS)
                return false;

            certificateCache.clear();
            negativeCertificateCache.clear();
            certificateCache.addAll(getSMEVCertificates());
            lastCacheUpdate = System.currentTimeMillis();

            boolean found = certificateCache.contains(certificate);
            if (!found) {
                negativeCertificateCache.add(certificate);
            }
            return found;
        }
    }
}
