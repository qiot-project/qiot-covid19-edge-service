package com.redhat.qiot.service.sensor.queries;

import com.redhat.qiot.domain.SensorBean;

public interface SensorQuery {
    SensorBean querySensor();

    SensorQueryEnum getSensorQueryEnum();
}
