/**
 * 
 */

package io.qiot.covid19.edge.service.localization;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * @author abattagl
 *
 */
@Path("/")
@RegisterRestClient(configKey = "nominatim-api")
public interface NominatimServiceClient {

    @GET
    @Path("/search")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    String query(@QueryParam("q") String address,@QueryParam("format") String format,
            @QueryParam("addressdetails") int detailLevel)
            throws Exception;

}
