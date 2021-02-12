package io.qiot.covid19.edge.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RegisterBean {
    public String stationId;
    public String trustStorePassword;
}
