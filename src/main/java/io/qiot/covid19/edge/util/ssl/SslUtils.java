package io.qiot.covid19.edge.util.ssl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

@ApplicationScoped
public class SslUtils {

    /**
     * Create a SSLSocketFactory with the specified TrustStore (for CA chain
     * certificate) and the specified KeyStore (for the client certificate)
     *
     * @param tsFile
     *            Trust Store file path created with KeyTool
     * @param tsPass
     *            Trust Store password
     * @param ksFile
     *            Key Store file path (PKCS12 format)
     * @param ksPass
     *            Key Store password
     *
     * @return SSLFactory
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     * @throws Exception
     */
    public SSLSocketFactory getSocketFactory(final String tsFile,
            final String tsPass, final String ksFile, final String ksPass)
            throws UnrecoverableKeyException, KeyManagementException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException,
            IOException {

        return getSSLContext(tsFile, tsPass, ksFile, ksPass).getSocketFactory();

    }

    public SSLContext getSSLContext(final String tsFile, final String tsPass,
            final String ksFile, final String ksPass)
            throws NoSuchAlgorithmException, CertificateException, IOException,
            KeyStoreException, UnrecoverableKeyException,
            KeyManagementException {
        if (tsFile == null && ksFile == null)
            return null;

        InputStream jksFile = new FileInputStream(tsFile);
        InputStream p12File = new FileInputStream(ksFile);

        KeyStore caKeyStore = KeyStore.getInstance("JKS");
        caKeyStore.load(jksFile, tsPass.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(caKeyStore);

        KeyStore clientKeyStore = KeyStore.getInstance("pkcs12");
        clientKeyStore.load(p12File, ksPass.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, ksPass.toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(),
                new java.security.SecureRandom());

        return context;
    }
}
