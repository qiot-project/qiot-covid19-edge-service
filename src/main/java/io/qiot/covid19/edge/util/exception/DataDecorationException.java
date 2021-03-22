/**
 * 
 */
package io.qiot.covid19.edge.util.exception;

/**
 * @author andreabattaglia
 *
 */
public class DataDecorationException extends Exception {

    public DataDecorationException() {
        super();
    }

    public DataDecorationException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataDecorationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDecorationException(String message) {
        super(message);
    }

    public DataDecorationException(Throwable cause) {
        super(cause);
    }

}
