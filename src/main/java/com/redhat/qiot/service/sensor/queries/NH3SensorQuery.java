/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.NH3)
@RequestScoped
public class NH3SensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASE_URI + SensorQueryEnum.NH3.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.NH3;
    }

}
