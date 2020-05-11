package com.redhat.qiot.service.jms;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Session;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.qiot.domain.SensorBean;
import com.redhat.qiot.service.sensor.SensorService;
import com.redhat.qiot.service.sensor.queries.SensorQueryEnum;

/**
 * A bean producing random prices every 5 seconds and sending them to the prices
 * JMS queue.
 */
@ApplicationScoped
public class NH3DataProducer {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Inject
    Logger LOGGER;

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    SensorService sensorService;

    public void sendData() {
	LOGGER.info("Producing message...");
//	SensorQuery sensorQuery = sensorQueryFactory.createSensorQuery(SensorQueryEnum.NH3);
	try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
	    String ququeName = SensorQueryEnum.NH3.name().toLowerCase();
	    JMSProducer producer = context.createProducer();
	    SensorBean sensorData = sensorService.querySensor(SensorQueryEnum.NH3);
	    String messagePayload = MAPPER.writeValueAsString(sensorData);

	    producer.send(context.createQueue(ququeName), context.createTextMessage(messagePayload));
	    LOGGER.info(
		    "Message with payload: " + messagePayload + " successfully sent to queue \"" + ququeName + "\"");
	} catch (JsonProcessingException e) {
	    LOGGER.error(e.getMessage());
	    e.printStackTrace();
	}
    }
}
