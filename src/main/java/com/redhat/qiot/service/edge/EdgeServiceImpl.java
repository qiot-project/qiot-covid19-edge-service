/**
 * 
 */
package com.redhat.qiot.service.edge;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

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

    private final AtomicInteger counter;

    @Inject
    Logger LOGGER;

    @Inject
    SensorService sensorService;

    public EdgeServiceImpl() {
	counter = new AtomicInteger();
    }

    void onStart(@Observes StartupEvent ev) {
	LOGGER.info("The application is starting...{}");
    }

    @Scheduled(every = "1s")
    void querySensor() {
	LOGGER.info("\n\n\n\n\nScheduled execution #" + counter.incrementAndGet() + " (frequency is 1 sec)\n\n");

//		LOGGER.info("Server response : \n");
	for (SensorQueryEnum sensor : SensorQueryEnum.values())
	    LOGGER.info(sensor.name() + ": " + sensorService.querySensor(sensor).toString());
    }

    void onStop(@Observes ShutdownEvent ev) {
	LOGGER.info("The application is stopping... {}");
    }
}
