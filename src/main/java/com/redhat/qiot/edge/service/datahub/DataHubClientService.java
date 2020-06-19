/**
 * 
 */

package com.redhat.qiot.edge.service.datahub;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


/**
 * @author abattagl
 *
 */
@Path("/v1")
@RegisterRestClient(configKey = "datahub-api")
public interface DataHubClientService {

    @PUT
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    int register(@QueryParam("stationData") String stationData)
            throws Exception;

    @DELETE
    @Path("/register")
    @Produces(MediaType.TEXT_PLAIN)
    void unregister(@QueryParam("id") int id) throws Exception;
}
