package com.googlecode.tlb.support.cruise.security;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthSSLX509TrustManagerFactory {
    private static final Log LOG = LogFactory.getLog(AuthSSLX509TrustManagerFactory.class);
    private final File truststore;
    private final String truststorePassword;

    public AuthSSLX509TrustManagerFactory(File truststore, String truststorePassword) {
        this.truststore = truststore;
        this.truststorePassword = truststorePassword;
    }

    public TrustManager[] trustManagers() {
        try {
            KeyStore store = new KeyStoreManager().loadOrEmpty(truststore, truststorePassword);
            if (LOG.isDebugEnabled()) { logKeyStore(store); }
            return setStoreOnTrustManagers(store);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't retrieve certificates to trust.", e);
        }
    }

    private TrustManager[] setStoreOnTrustManagers(KeyStore keystore)
            throws KeyStoreException, NoSuchAlgorithmException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        LOG.trace("Initializing trust manager");
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(keystore);
        return selfSignedX509WrappedTrustManagers(keystore, tmfactory);
    }

    private TrustManager[] selfSignedX509WrappedTrustManagers(KeyStore keystore, TrustManagerFactory tmfactory)
            throws NoSuchAlgorithmException, KeyStoreException {
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        for (int i = 0; i < trustmanagers.length; i++) {
            if (trustmanagers[i] instanceof X509TrustManager) {
                trustmanagers[i] = new SelfSignedCertificateX509TrustManager(keystore,
                        (X509TrustManager) trustmanagers[i], truststore, truststorePassword);
            }
        }
        return trustmanagers;
    }

    private void logKeyStore(KeyStore store) throws KeyStoreException {
        Enumeration aliases = store.aliases();
        while (aliases.hasMoreElements()) {
            String alias = (String) aliases.nextElement();
            LOG.debug("Trusted certificate '" + alias + "':");
            Certificate trustedcert = store.getCertificate(alias);
            if (trustedcert != null && trustedcert instanceof X509Certificate) {
                X509Certificate cert = (X509Certificate) trustedcert;
                LOG.trace("  Subject DN: " + cert.getSubjectDN());
                LOG.trace("  Signature Algorithm: " + cert.getSigAlgName());
                LOG.trace("  Valid from: " + cert.getNotBefore());
                LOG.trace("  Valid until: " + cert.getNotAfter());
                LOG.trace("  Issuer: " + cert.getIssuerDN());
            }
        }
    }
}
