/**
 * 
 */
package com.redhat.qiot.service.sensor;

import com.redhat.qiot.domain.SensorBean;
import com.redhat.qiot.service.sensor.queries.SensorQueryEnum;

/**
 * @author abattagl
 *
 */
public interface SensorService {

	SensorBean querySensor(SensorQueryEnum sensor);
}
