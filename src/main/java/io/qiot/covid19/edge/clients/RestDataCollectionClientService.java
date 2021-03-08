/**
 * 
 */

package io.qiot.covid19.edge.clients;


import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


/**
 * @author andreabattaglia
 *
 */
@Path("/v1")
@RegisterRestClient(configKey = "datacollection-api")
public interface RestDataCollectionClientService {

    @PUT
    @Path("/gas")
    @Produces(MediaType.APPLICATION_JSON)
    void sendGas(@QueryParam("data") String data) throws Exception;

    @PUT
    @Path("/pollution")
    @Produces(MediaType.APPLICATION_JSON)
    void sendPollution(@QueryParam("data") String data)
            throws Exception;
}
