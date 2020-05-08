package com.redhat.qiot.service.sensor.queries;

public enum SensorQueryEnum {
    // WEATHER
    /**
     * 
     */
    COMPENSATEDTEMPERATURE("/weather/compensatedtemperature"), 
    /**
     * 
     */
    HUMIDITY("/weather/humidity"),
    /**
     * 
     */
    PRESSURE("/weather/pressure"), 
    /**
     * 
     */
    TEMPERATURE("/weather/temperature"),
    // LIGHT
    /**
     * 
     */
    LIGHT("/light/light"), /**
     * 
     */
    PROXIMITY("/light/proximity"),
    // GAS
    /**
     * 
     */
    ADC("/gas/adc"), /**
     * 
     */
    NH3("/gas/nh3"), /**
     * 
     */
    OXIDISING("/gas/oxidising"), /**
     * 
     */
    REDUCING("/gas/reducing");
    // NOISE
    // TODO: complete NOISE

    private final String path;

    private SensorQueryEnum(String path) {
	this.path = path;
    }

    public String getPath() {
	return path;
    }

}
