package io.qiot.covid19.edge.util.producers;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;

import io.qiot.covid19.edge.util.ssl.SslUtils;

/*
Fixed #2086 by using client-maven-plugin:0.1.27. This linked with the missing native library, which was missing when calling native-image directly from command line.
Fixed access to i18n msg strings by adding -H:IncludeResourceBundles=org.eclipse.paho.client.mqttv3.internal.nls.logcat,org.eclipse.paho.client.mqttv3.internal.nls.messages to native-image.properties
Set up -H:ReflectionConfigurationResources=reflection-config.json with reflection info for org.eclipse.paho.client.mqttv3.logging.JSR47Logger, org.eclipse.paho.client.mqttv3.internal.MessageCatalog, org.eclipse.paho.client.mqttv3.internal.ResourceBundleCatalog, and java.util.ResourceBundle.
Instantiated MqttClient with persistence = null, since the default persistence class couldn't access user home for some reason (null pointer on MqttDefaultFilePersistence line 259). 
 */
/**
 * The Class MqttConnectionProducer.
 *
 * @author andreabattaglia
 */
@ApplicationScoped
public class MqttConnectionProducer {

    /** The logger. */
    @Inject
    Logger LOGGER;

    /** The ssl utils. */
    @Inject
    SslUtils sslUtils;

    // @ConfigProperty(name = "qiot.mqtt.client.connection.generated-client-id")
    /** The host. */
    // boolean generatedClientId;
    @ConfigProperty(name = "qiot.mqtt.client.connection.host")
    String host;

    /** The port. */
    @ConfigProperty(name = "qiot.mqtt.client.connection.port")
    short port;

    /** The enable ssl. */
    @ConfigProperty(name = "qiot.mqtt.client.connection.ssl")
    boolean enableSsl;

    /** The keystore location. */
    @ConfigProperty(name = "qiot.mqtt.client.connection.ssl.keystore.location")
    String keystoreLocation;

    /** The keystore password. */
    @ConfigProperty(name = "qiot.mqtt.client.connection.ssl.keystore.password")
    String keystorePassword;

    /** The truststore location. */
    @ConfigProperty(
            name = "qiot.mqtt.client.connection.ssl.truststore.location")
    String truststoreLocation;

    /** The truststore password. */
    @ConfigProperty(
            name = "qiot.mqtt.client.connection.ssl.truststore.password")
    String truststorePassword;

    /** The options. */
    MqttConnectOptions options;

    /** The client id. */
    String clientId;

    /** The buffer opts. */
    DisconnectedBufferOptions bufferOpts;

    /** The ssl context. */
    SSLContext sslContext;

    /** The trust manager factory. */
    TrustManagerFactory trustManagerFactory;

    /** The key store. */
    KeyStore keyStore;

    /** The client. */
    MqttAsyncClient client;

    /**
     * Instantiates a new mqtt connection producer.
     */
    public MqttConnectionProducer() {
        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(10);
        options.setConnectionTimeout(30);
        options.setMaxInflight(200);
        clientId = MqttClient.generateClientId();
        options.setUserName(clientId);

        bufferOpts = new DisconnectedBufferOptions();
        bufferOpts.setBufferEnabled(true);
        bufferOpts.setBufferSize(1000);
        bufferOpts.setDeleteOldestMessages(true);
        bufferOpts.setPersistBuffer(false);
    }

    /**
     * Inits the.
     *
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    void init() throws Exception {

        client = new MqttAsyncClient(host + ":" + port, clientId,
                new MemoryPersistence());
    }

    /**
     * Produce mqtt connection.
     *
     * @return the mqtt async client
     */
    @Produces
    public MqttAsyncClient produceMqttConnection() {
        if (!client.isConnected()) {
            if (enableSsl) {
                SSLSocketFactory socketFactory;
                try {
                    socketFactory = sslUtils.getSocketFactory(
                            truststoreLocation, truststorePassword,
                            keystoreLocation, keystorePassword);
                } catch (UnrecoverableKeyException | KeyManagementException
                        | NoSuchAlgorithmException | CertificateException
                        | KeyStoreException | IOException e) {
                    throw new RuntimeException(e);
                }
                options.setSocketFactory(socketFactory);

            }

            client.setBufferOpts(bufferOpts);

            try {
                client.connect(options);
            } catch (MqttSecurityException e) {
                throw new RuntimeException(e);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return client;
    }

    /**
     * Destroy.
     */
    @PreDestroy
    void destroy() {
        if (client != null && client.isConnected())
            try {
                // client.disconnect().waitForCompletion(5_000);
                client.close();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
    }
}
