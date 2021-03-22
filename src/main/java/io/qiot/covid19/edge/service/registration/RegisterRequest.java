package io.qiot.covid19.edge.service.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "serial", "name", "longitude", "latitude",
        "keyStorePassword" })
public class RegisterRequest {

    @JsonProperty("serial")
    public String serial;
    @JsonProperty("name")
    public String name;
    @JsonProperty("longitude")
    public Double longitude;
    @JsonProperty("latitude")
    public Double latitude;
    /**
     * KeyStore Password
     * 
     */
    @JsonProperty("keyStorePassword")
    @JsonPropertyDescription("KeyStore Password")
    public String keyStorePassword;

//    @JsonProperty("serial")
//    public String getSerial() {
//        return serial;
//    }
//
//    @JsonProperty("serial")
//    public void setSerial(String serial) {
//        this.serial = serial;
//    }
//
//    @JsonProperty("name")
//    public String getName() {
//        return name;
//    }
//
//    @JsonProperty("name")
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @JsonProperty("longitude")
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    @JsonProperty("longitude")
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//    @JsonProperty("latitude")
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    @JsonProperty("latitude")
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    /**
//     * KeyStore Password
//     * 
//     */
//    @JsonProperty("keyStorePassword")
//    public String getKeyStorePassword() {
//        return keyStorePassword;
//    }
//
//    /**
//     * KeyStore Password
//     * 
//     */
//    @JsonProperty("keyStorePassword")
//    public void setKeyStorePassword(String keyStorePassword) {
//        this.keyStorePassword = keyStorePassword;
//    }

}
