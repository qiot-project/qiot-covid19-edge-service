package com.redhat.qiot.edge.service.datahub;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.rabbitmq.RabbitMQClient;
import io.vertx.mutiny.rabbitmq.RabbitMQConsumer;
import io.vertx.rabbitmq.RabbitMQOptions;

@ApplicationScoped
public class RabbitMQClientTest {

    @ConfigProperty(name = "edge.integration.server.host")
    String rabbitMQHost;
    @ConfigProperty(name = "edge.integration.server.port")
    int rabbitMQPort;
    @ConfigProperty(name = "edge.integration.server.username")
    String rabbitMQUsername;
    @ConfigProperty(name = "edge.integration.server.password")
    String rabbitMQPassword;

    @Inject
    Logger LOGGER;
    private String GAS_QUEUE = "gas";
    private String POLLUTION_QUEUE = "pollution";

    @Inject
    Vertx vertx;

    @Inject
    MQTTDataHubCollectionClientService mqttDataHubCollectionClientService;

    private RabbitMQClient client;
    private RabbitMQConsumer gasConsumer;
    private RabbitMQConsumer pollutionConsumer;

    void onStart(@Observes StartupEvent ev) {

    }

    @PostConstruct
    void init() {
        LOGGER.info("messaging client init...");
        /*
         * CLIENT
         */
        client = RabbitMQClient.create(vertx, new RabbitMQOptions()
                // .setUri(rabbitMQUri)
                .setHost(rabbitMQHost).setPort(rabbitMQPort)
                .setUser(rabbitMQUsername).setPassword(rabbitMQPassword)
                .setAutomaticRecoveryEnabled(true)
                .setConnectionRetries(Integer.MAX_VALUE));
        client.startAndAwait();

        /*
         * DESTINATIONS
         */

        client.queueDeclareAndAwait(GAS_QUEUE, true, false, false);
        // try {
        // createQueue(POLLUTION_QUEUE);
        // } catch (KeyManagementException | NoSuchAlgorithmException
        // | URISyntaxException | IOException | TimeoutException e) {
        // LOGGER.error("An error occurred declaring POLLUTION queue", e);
        // }
        client.queueDeclareAndAwait(POLLUTION_QUEUE, true, false, false);

        // private void createQueue( String queue) throws
        // KeyManagementException, NoSuchAlgorithmException, URISyntaxException,
        // IOException, TimeoutException {
        // ConnectionFactory factory = new ConnectionFactory();
        // factory.setUri(rabbitMQUri);
        // try (Channel channel = factory.newConnection().createChannel()) {
        // channel.queueDeclare(queue, true, false, false, null);
        // }

        /*
         * CONSUMERS
         */
        gasConsumer = client.basicConsumerAndAwait(GAS_QUEUE);
        gasConsumer.toMulti().onItem().transform(m -> m.body().toString())
                .subscribe().with(//
                        item ->
                        // LOGGER.info("consumed message {}",item),
                        mqttDataHubCollectionClientService.sendGas(item), //
                        failure -> LOGGER.error(
                                "An error occurred receiveing GAS telemetry",
                                failure)//
                );

        pollutionConsumer = client.basicConsumerAndAwait(POLLUTION_QUEUE);
        pollutionConsumer.toMulti().onItem().transform(m -> m.body().toString())
                .subscribe().with(//
                        item ->
                        // LOGGER.info("consumed message {}",item),
                        mqttDataHubCollectionClientService.sendPollution(item), //
                        failure -> LOGGER.error(
                                "An error occurred receiveing POLLUTION telemetry",
                                failure)//
                );

    }

    @PreDestroy
    void destroy() {
        LOGGER.info("messaging client destroy...");
        gasConsumer.cancelAndAwait();
        pollutionConsumer.cancelAndAwait();
        client.stopAndAwait();
    }

}