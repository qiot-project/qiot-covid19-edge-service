/**
 * 
 */


package io.qiot.covid19.edge.service.sensor;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.qiot.covid19.edge.domain.StationSerialId;


/**
 * @author andreabattaglia
 *
 */
@Path("/")
@RegisterRestClient(configKey = "sensor-api")
public interface SensorServiceClient {

    @GET
    @Path("/system/id")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    StationSerialId getSerialId() throws Exception;

    @GET
    @Path("/gas/all")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    String getGasMeasurement() throws Exception;

    @GET
    @Path("/particulates/all")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    String getParticulatesMeasurement() throws Exception;
}
