/**
 * 
 */
package com.redhat.qiot.edge.util.decorator;


/**
 * @author Andrea
 *
 */
public interface MeasurementDecorator {
    String decorate(String stationId, String measurement);
}
