/**
 * 
 */
package io.qiot.covid19.edge.service.telemetry;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.mqtt.MqttException;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.mqtt.MqttClient;

/**
 * The Class MqttEndpointClient.
 *
 * @author andreabattaglia
 */
@ApplicationScoped
public class TelemetryService {

    /** The logger. */
    @Inject
    Logger LOGGER;

    @ConfigProperty(name = "qiot.mqtt.client.topic.gas.topic")
    String gasTopicName;
    @ConfigProperty(name = "qiot.mqtt.client.topic.gas.qos")
    int gasTopicQoS;

    @ConfigProperty(name = "qiot.mqtt.client.topic.pollution.topic")
    String pollutionTopicName;
    @ConfigProperty(name = "qiot.mqtt.client.topic.pollution.qos")
    int pollutionTopicQoS;

    @Inject
    MqttClient mqttClient;

    // IMqttToken gasTopic;
    // IMqttToken pollutionTopic;

    /**
     * Inits the.
     *
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    void init() throws Exception {
        Map<String, Integer> topicsMap = new HashMap<>();
        topicsMap.put(gasTopicName, gasTopicQoS);
        topicsMap.put(pollutionTopicName, pollutionTopicQoS);

        mqttClient.subscribeAndAwait(topicsMap);
        
        LOGGER.info("Subscribed to target topics.");
    }
    
    public void getReady() {
        
    }

    /**
     * Send gas.
     *
     * @param data
     *            the data
     * @throws MqttException
     * @throws MqttPersistenceException
     */
    public void sendGas(String data) {
        LOGGER.info("Sending out GAS measurement");

        mqttClient.publishAndAwait(gasTopicName, Buffer.buffer(data),
                MqttQoS.valueOf(gasTopicQoS), false, false);
    }

    /**
     * Send pollution.
     *
     * @param data
     *            the data
     * @throws MqttPersistenceException,
     *             MqttException
     */
    public void sendPollution(String data) {
        LOGGER.info("Sending out POLLUTION measurement");

        mqttClient.publishAndAwait(pollutionTopicName, Buffer.buffer(data),
                MqttQoS.valueOf(pollutionTopicQoS), false, false);
    }
}
