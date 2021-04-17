/**
 * 
 */
package io.qiot.covid19.edge.util.decorator;

import io.qiot.covid19.edge.util.exception.DataDecorationException;

/**
 * @author Andrea
 *
 */
public interface TelemetryDecorator {
    String decorate(String stationId, String measurement)
            throws DataDecorationException;
}
