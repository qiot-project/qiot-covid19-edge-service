package io.qiot.covid19.edge.service.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "truststore", "keystore" })
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

}
