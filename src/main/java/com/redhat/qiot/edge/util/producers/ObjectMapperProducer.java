package com.redhat.qiot.edge.util.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;


import com.fasterxml.jackson.databind.ObjectMapper;


@ApplicationScoped
public class ObjectMapperProducer {

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Produces
    public ObjectMapper getLogger(final InjectionPoint ip) {
        return MAPPER;
    }
}