/**
 * 
 */
package io.qiot.covid19.edge.util.decorator;


/**
 * @author Andrea
 *
 */
public interface TelemetryDecorator {
    String decorate(String stationId, String measurement);
}
