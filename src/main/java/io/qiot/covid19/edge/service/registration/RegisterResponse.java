package io.qiot.covid19.edge.service.registration;

/**
 * 
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "id", "truststore", "keystore" })
public class RegisterResponse {

    /**
     * 
     * (Required)
     * 
     */
//    @JsonProperty("id")
//    @JsonPropertyDescription("")
    public String id;
    /**
     * 
     * (Required)
     * 
     */
//    @JsonProperty("truststore")
//    @JsonPropertyDescription("")
    public String truststore;
    /**
     * 
     * (Required)
     * 
     */
    // @JsonProperty("keystore")
    // @JsonPropertyDescription("")
    public String keystore;

}
