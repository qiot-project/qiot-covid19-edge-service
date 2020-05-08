/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.PROXIMITY)
@RequestScoped
public class ProximitySensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASIC_URI + SensorQueryEnum.PROXIMITY.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.PROXIMITY;
    }

}
