package com.googlecode.tlb.support.cruise.security;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class SelfSignedCertificateX509TrustManager implements X509TrustManager {

    private static final Log LOG = LogFactory.getLog(SelfSignedCertificateX509TrustManager.class);
    private final KeyStore truststore;
    private final X509TrustManager defaultTrustManager;
    private final File truststorePath;
    private final String truststorePassword;
    public static final String CRUISE_SERVER = "cruise-server";

    public SelfSignedCertificateX509TrustManager(KeyStore truststore, X509TrustManager defaultTrustManager,
                                                 File truststorePath, String truststorePassword)
            throws NoSuchAlgorithmException, KeyStoreException {
        super();
        this.truststore = truststore;
        this.defaultTrustManager = defaultTrustManager;
        this.truststorePath = truststorePath;
        this.truststorePassword = truststorePassword;
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
     */
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (LOG.isDebugEnabled() && certificates != null) {
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                LOG.info(" Client certificate " + (c + 1) + ":");
                LOG.info("  Subject DN: " + cert.getSubjectDN());
                LOG.info("  Signature Algorithm: " + cert.getSigAlgName());
                LOG.info("  Valid from: " + cert.getNotBefore());
                LOG.info("  Valid until: " + cert.getNotAfter());
                LOG.info("  Issuer: " + cert.getIssuerDN());
            }
        }
        defaultTrustManager.checkClientTrusted(certificates, authType);
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
     */
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (LOG.isDebugEnabled() && certificates != null) {
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                LOG.info(" Server certificate " + (c + 1) + ":");
                LOG.info("  Subject DN: " + cert.getSubjectDN());
                LOG.info("  Signature Algorithm: " + cert.getSigAlgName());
                LOG.info("  Valid from: " + cert.getNotBefore());
                LOG.info("  Valid until: " + cert.getNotAfter());
                LOG.info("  Issuer: " + cert.getIssuerDN());
            }
        }

        try {
            if ((certificates != null) && (certificates.length == 1) && !truststore.containsAlias(CRUISE_SERVER)) {
                certificates[0].checkValidity();
                updateKeystore(CRUISE_SERVER, certificates[0]);
            } else {
                defaultTrustManager.checkServerTrusted(certificates, authType);
            }
        } catch (KeyStoreException ke) {
            throw new RuntimeException("Couldn't access keystore while checking server's certificate", ke);
        }
    }


    private void updateKeystore(String alias, Certificate chain) {
        FileOutputStream fileOutputStream = null;
        try {
            truststore.setCertificateEntry(alias, chain);
            fileOutputStream = new FileOutputStream(truststorePath);
            truststore.store(fileOutputStream, truststorePassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Coudn't update keystore with certificate with alias " + alias, e);
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    /**
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return this.defaultTrustManager.getAcceptedIssuers();
    }
}
