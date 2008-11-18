package com.googlecode.tlb.support.cruise.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

import org.apache.commons.io.IOUtils;
import static com.googlecode.tlb.utils.ExceptionUtils.bombIfNull;
import static com.googlecode.tlb.utils.ExceptionUtils.bomb;

public class KeyStoreManager {
    private static final String KEYSTORE_TYPE = "JKS";

    private KeyStore lazyLoadedStore;

    public KeyStoreManager() {
    }

    public void storeX509Certificate(String friendlyName, File storeFile, String passwd, Registration entry)
            throws Exception {
        lazyLoadedStore = KeyStore.getInstance(KEYSTORE_TYPE);
        loadStore(lazyLoadedStore);
        storeCertificate(friendlyName, storeFile, passwd, entry);
    }

    public void storeCACertificate(File storeFile, String password, Certificate caCertificate, Registration entry)
            throws Exception {
        lazyLoadedStore = KeyStore.getInstance(KEYSTORE_TYPE);
        loadStore(lazyLoadedStore);

        lazyLoadedStore.setCertificateEntry("ca-cert", caCertificate);
        lazyLoadedStore.setEntry("ca-intermediate", entry.asKeyStoreEntry(),
                new KeyStore.PasswordProtection(password.toCharArray()));
        writeStore(storeFile, password);
    }

    public void storeCertificate(String friendlyName, File storeFile, String passwd, Registration entry)
            throws Exception {
        KeyStore storeToSave = loadOrEmpty(storeFile, passwd);
        bombIfNull(storeToSave, "Store not yet initialized");
        storeToSave.setKeyEntry(friendlyName, entry.getPrivateKey(), passwd.toCharArray(), entry.getChain());
        writeStore(storeToSave, storeFile, passwd);
    }

    public boolean hasCertificates(String friendlyName, File storeFile, String passwd) {
        try {
            KeyStore keyStore = loadOrEmpty(storeFile, passwd);
            bombIfNull(keyStore, "Store not yet initialized");
            return keyStore.getCertificateChain(friendlyName) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteEntry(String friendlyName, File storeFile, String passwd) throws Exception {
        bombIfNull(lazyLoadedStore, "Store not yet initialized");
        lazyLoadedStore.deleteEntry(friendlyName);
        writeStore(storeFile, passwd);
    }

    private void loadStore(KeyStore store) {
        try {
            store.load(null, null);
        } catch (Exception e) {
            throw bomb(e);
        }
    }

    private void writeStore(File storeFile, String password) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = maybeOutputStream(storeFile);
            lazyLoadedStore.store(fileOutputStream, maybePassword(password));
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    private void writeStore(KeyStore store, File storeFile, String password) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = maybeOutputStream(storeFile);
            store.store(fileOutputStream, maybePassword(password));
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    public void preload(File keystoreFile, String password) throws Exception {
        this.lazyLoadedStore = loadOrEmpty(keystoreFile, password);
    }

    @Deprecated // Need to move the logic into this class so we don't have to touch the KeyStore in our code
    public KeyStore loadOrEmpty(File keystoreFile, String password) throws Exception {
        KeyStore store = tryLoad(keystoreFile, password);
        if (store == null) {
            store = load(null, password);
        }
        return store;
    }

    @Deprecated // Need to move the logic into this class so we don't have to touch the KeyStore in our code
    public KeyStore load(File keystoreFile, String password) throws Exception {
        FileInputStream inputStream = null;
        try {
            KeyStore store = KeyStore.getInstance(KEYSTORE_TYPE);
            inputStream = maybeInputStream(keystoreFile);
            store.load(inputStream, maybePassword(password));
            return store;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Deprecated // Need to move the logic into this class so we don't have to touch the KeyStore in our code
    public KeyStore tryLoad(File keystoreFile, String password) {
        try {
            return load(keystoreFile, password);
        } catch (Exception e) {
            return null;
        }
    }

    private FileInputStream maybeInputStream(File file) throws FileNotFoundException {
        return file == null ? null : new FileInputStream(file);
    }

    private FileOutputStream maybeOutputStream(File file) throws FileNotFoundException {
        return file == null ? null : new FileOutputStream(file);
    }

    private char[] maybePassword(String password) {
        return password == null ? null : password.toCharArray();
    }
}
