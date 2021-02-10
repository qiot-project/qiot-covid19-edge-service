/**
 * 
 */

package com.redhat.qiot.edge.service.registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.qiot.edge.util.exceptionmapper.CustomExceptionMapper;

@Path("/v1")
@RegisterRestClient(configKey = "registration-api")
@RegisterProvider(CustomExceptionMapper.class)
public interface RegistrationServiceClient {

    @PUT
    @Path("/register")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response register(
            @QueryParam("serial") String serial,
            @QueryParam("name") String name,
            @QueryParam("longitude") double longitude,
            @QueryParam("latitude") double latitude) throws Exception;
}
