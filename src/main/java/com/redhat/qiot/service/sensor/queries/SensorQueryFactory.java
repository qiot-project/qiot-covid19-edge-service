package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class SensorQueryFactory {
    @Inject
    Logger LOGGER;

    @Inject
    @Any
    Instance<SensorQuery> sensorQueryInstance;

    void onStart(@Observes StartupEvent ev) {
	LOGGER.info("Available Sensor Queries");
	for (SensorQuery instance : sensorQueryInstance) {
	    LOGGER.info(instance.getSensorQueryEnum().name());
	}
    }

    public SensorQuery createSensorQuery(SensorQueryEnum _sensorQuery) {
	Instance<SensorQuery> instance = this.sensorQueryInstance.select(new SensorQueryProviderAnnotationLiteral(_sensorQuery));

//	if (!instance.isResolvable()) {
//	    throw new IllegalArgumentException("sensor query " + _sensorQuery + " not supported");
//	}

	return instance.get();
    }
}