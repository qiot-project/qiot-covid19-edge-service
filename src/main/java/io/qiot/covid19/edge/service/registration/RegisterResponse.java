package io.qiot.covid19.edge.service.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "truststore", "keystore" })
@RegisterForReflection
public class RegisterResponse {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("")
    public String id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("truststore")
    @JsonPropertyDescription("")
    public String truststore;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("keystore")
    @JsonPropertyDescription("")
    public String keystore;
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RegisterResponse [id=");
        builder.append(id);
        builder.append(", truststore=");
        builder.append(truststore);
        builder.append(", keystore=");
        builder.append(keystore);
        builder.append("]");
        return builder.toString();
    }

    
}
