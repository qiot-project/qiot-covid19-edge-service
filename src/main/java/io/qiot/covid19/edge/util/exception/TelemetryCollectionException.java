/**
 * 
 */
package io.qiot.covid19.edge.util.exception;

/**
 * @author andreabattaglia
 *
 */
public class TelemetryCollectionException extends Exception {

    public TelemetryCollectionException() {
        super();
    }

    public TelemetryCollectionException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TelemetryCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelemetryCollectionException(String message) {
        super(message);
    }

    public TelemetryCollectionException(Throwable cause) {
        super(cause);
    }

}
