/**
 * 
 */

package io.qiot.covid19.edge.service.registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.qiot.covid19.edge.util.exceptionmapper.CustomExceptionMapper;

@Path("/v1/register")
@RegisterRestClient(configKey = "registration-api")
@RegisterProvider(CustomExceptionMapper.class)
public interface RegistrationServiceClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    RegisterResponse register(RegisterRequest data);
}
