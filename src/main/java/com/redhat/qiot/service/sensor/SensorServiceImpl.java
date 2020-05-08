/**
 * 
 */
package com.redhat.qiot.service.sensor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.redhat.qiot.domain.SensorBean;
import com.redhat.qiot.service.sensor.queries.SensorQuery;
import com.redhat.qiot.service.sensor.queries.SensorQueryEnum;
import com.redhat.qiot.service.sensor.queries.SensorQueryFactory;

/**
 * @author abattagl
 *
 */
@ApplicationScoped
class SensorServiceImpl implements SensorService {

    @Inject
     Logger LOGGER;

    @Inject
     SensorQueryFactory sensorQueryFactory;

    @Override
    public SensorBean querySensor(SensorQueryEnum sensor) {

	SensorQuery sensorQuery = sensorQueryFactory.createSensorQuery(sensor);
	return sensorQuery.querySensor();

    }
}
