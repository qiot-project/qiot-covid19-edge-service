/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.PRESSURE)
@RequestScoped
public class PressureSensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASIC_URI + SensorQueryEnum.PRESSURE.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.PRESSURE;
    }

}
