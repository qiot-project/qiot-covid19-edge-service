package com.redhat.qiot.edge.util.exceptionmapper;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.slf4j.Logger;

public class CustomExceptionMapper
        implements ResponseExceptionMapper<Throwable> {

    @Inject
    Logger LOGGER;

    @Override
    public Throwable toThrowable(Response response) {
        try {
            response.bufferEntity();
        } catch (Exception ignored) {
            LOGGER.error(
                    "An error occurred reading the response from the registration-service.",
                    ignored);
        }
        return new WebApplicationException(
                "Unknown error, status code " + response.getStatus(), response);
    }

}