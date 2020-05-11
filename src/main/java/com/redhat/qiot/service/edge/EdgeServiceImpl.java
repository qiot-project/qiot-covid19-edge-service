/**
 * 
 */
package com.redhat.qiot.service.edge;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;

import org.slf4j.Logger;

import com.redhat.qiot.service.jms.NH3DataProducer;
import com.redhat.qiot.service.sensor.SensorService;
import com.redhat.qiot.service.sensor.queries.SensorQueryEnum;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

/**
 * @author abattagl
 *
 */
@Startup
@ApplicationScoped
class EdgeServiceImpl implements EdgeService {

    @Inject
    ConnectionFactory connectionFactory;

//    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final AtomicLong counter;

    @Inject
    Logger LOGGER;

    @Inject
    SensorService sensorService;
    
    @Inject
    NH3DataProducer nH3DataProducer;
    
    public EdgeServiceImpl() {
	counter = new AtomicLong();
    }

    void onStart(@Observes StartupEvent ev) {
	LOGGER.info("The application is starting...{}");
    }

    @Scheduled(every = "3s")
    void run() {
//	querySensor();
	produceData();
    }
    
    void produceData() {
	nH3DataProducer.sendData();
    }
    void querySensor() {
	LOGGER.info("\n\n\n\n\nScheduled execution #" + counter.incrementAndGet() + " (frequency is 3 sec)\n\n");

//		LOGGER.info("Server response : \n");
	for (SensorQueryEnum sensor : SensorQueryEnum.values())
	    LOGGER.info(sensor.name() + ": " + sensorService.querySensor(sensor).toString());
    }

    void onStop(@Observes ShutdownEvent ev) {
	LOGGER.info("The application is stopping... {}");
    }
}
