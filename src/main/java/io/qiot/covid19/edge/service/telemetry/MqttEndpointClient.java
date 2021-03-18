/**
 * 
 */
package io.qiot.covid19.edge.service.telemetry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;

/**
 * The Class MqttEndpointClient.
 *
 * @author andreabattaglia
 */
@ApplicationScoped
public class MqttEndpointClient {

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
    MqttAsyncClient mqttClient;

    IMqttToken gasTopic;
    IMqttToken pollutionTopic;

    /**
     * Send gas.
     *
     * @param data
     *            the data
     * @throws MqttException
     * @throws MqttPersistenceException
     */
    public void sendGas(String data)
            throws MqttPersistenceException, MqttException {
        LOGGER.info("Sending out GAS measurement");

        MqttMessage v3Message = new MqttMessage(data.getBytes());
        v3Message.setQos(gasTopicQoS);
        v3Message.setRetained(true);
        IMqttDeliveryToken deliveryToken = mqttClient.publish(gasTopicName,
                v3Message);
        deliveryToken.waitForCompletion(5_000);
    }

    /**
     * Send pollution.
     *
     * @param data
     *            the data
     * @throws MqttPersistenceException,
     *             MqttException
     */
    public void sendPollution(String data)
            throws MqttPersistenceException, MqttException {
        LOGGER.info("Sending out PARTICULATES measurement");

        MqttMessage v3Message = new MqttMessage(data.getBytes());
        v3Message.setQos(pollutionTopicQoS);
        v3Message.setRetained(true);
        IMqttDeliveryToken deliveryToken = mqttClient
                .publish(pollutionTopicName, v3Message);
        deliveryToken.waitForCompletion(5_000);
    }
}
