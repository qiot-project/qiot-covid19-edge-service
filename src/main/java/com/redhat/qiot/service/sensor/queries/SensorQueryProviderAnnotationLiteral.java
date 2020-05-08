package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.util.AnnotationLiteral;

public class SensorQueryProviderAnnotationLiteral extends AnnotationLiteral<SensorQueryProvider> implements SensorQueryProvider {
    /**
     * 
     */
    private static final long serialVersionUID = 6622873591457044796L;
    private final SensorQueryEnum name;

    public SensorQueryProviderAnnotationLiteral(SensorQueryEnum _name) {
	this.name = _name;
    }

    @Override
    public SensorQueryEnum value() {
	return this.name;
    }
}