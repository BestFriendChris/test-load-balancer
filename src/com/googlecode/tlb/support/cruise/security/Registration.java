package com.googlecode.tlb.support.cruise.security;

import java.io.Serializable;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

public class Registration implements Serializable {
    private final PrivateKey privateKey;
    private final Certificate[] chain;
    private final String guid;

    public static Registration createNullPrivateKeyEntry() {
        return new Registration("", emptyPrivateKey());
    }

    public Registration(String guid, PrivateKey privateKey, Certificate... chain) {
        this.privateKey = privateKey;
        this.chain = chain;
        this.guid = guid;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return getFirstCertificate().getPublicKey();
    }

    public Certificate[] getChain() {
        return chain;
    }

    public String getGuid() {
        return guid;
    }

    public X509Certificate getFirstCertificate() {
        return (X509Certificate) chain[0];
    }

    public Date getCertificateNotBeforeDate() {
        return getFirstCertificate().getNotBefore();
    }

    private static PrivateKey emptyPrivateKey() {
        return new PrivateKey() {
            public String getAlgorithm() {
                return null;
            }

            public String getFormat() {
                return null;
            }

            public byte[] getEncoded() {
                return new byte[0];
            }
        };
    }

    public KeyStore.PrivateKeyEntry asKeyStoreEntry() {
        return new KeyStore.PrivateKeyEntry(privateKey, chain);
    }
}
