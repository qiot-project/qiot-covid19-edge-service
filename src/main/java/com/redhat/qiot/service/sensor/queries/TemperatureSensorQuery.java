/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.TEMPERATURE)
@RequestScoped
public class TemperatureSensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASIC_URI + SensorQueryEnum.TEMPERATURE.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.TEMPERATURE;
    }

}
