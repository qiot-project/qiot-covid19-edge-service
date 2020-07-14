/**
 * 
 */

package com.redhat.qiot.edge.service.datahub;


import javax.enterprise.context.ApplicationScoped;
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
@Path("/v1/register")
@RegisterRestClient(configKey = "datahub-api")
@ApplicationScoped
public interface DataHubClientService {

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    String register(@QueryParam("stationData") String stationData)
            throws Exception;

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    void unregister(@QueryParam("id") String id) throws Exception;
}
