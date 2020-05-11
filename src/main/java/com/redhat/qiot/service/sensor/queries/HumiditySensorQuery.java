/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.HUMIDITY)
@RequestScoped
public class HumiditySensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASE_URI + SensorQueryEnum.HUMIDITY.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.HUMIDITY;
    }

}
