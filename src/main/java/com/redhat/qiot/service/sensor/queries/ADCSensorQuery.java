/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.ADC)
@RequestScoped
public class ADCSensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASE_URI + SensorQueryEnum.ADC.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.ADC;
    }

}
