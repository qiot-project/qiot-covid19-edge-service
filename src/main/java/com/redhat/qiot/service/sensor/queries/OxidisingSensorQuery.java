/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.RequestScoped;

/**
 * @author abattagl
 *
 */
@SensorQueryProvider(SensorQueryEnum.OXIDISING)
@RequestScoped
public class OxidisingSensorQuery extends AbstractSensorQuery {

    @Override
    String getUriString() {
	return this.BASE_URI + SensorQueryEnum.OXIDISING.getPath();
    }

    @Override
    public SensorQueryEnum getSensorQueryEnum() {
	return SensorQueryEnum.OXIDISING;
    }

}
