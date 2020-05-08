/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.COMPENSATEDTEMPERATURE)
@RequestScoped
public class CompensatedTemperatureSensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASIC_URI + SensorQueryEnum.COMPENSATEDTEMPERATURE.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.COMPENSATEDTEMPERATURE;
    }

}
