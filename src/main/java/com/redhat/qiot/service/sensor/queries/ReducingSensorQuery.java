/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.REDUCING)
@RequestScoped
public class ReducingSensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASE_URI + SensorQueryEnum.REDUCING.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.REDUCING;
    }

}
