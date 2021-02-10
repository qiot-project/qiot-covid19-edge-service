package com.redhat.qiot.edge.service.registration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;

import com.redhat.qiot.edge.domain.RegisterBean;
import com.redhat.qiot.edge.service.station.StationService;

@ApplicationScoped
public class RegistrationService {
    @Inject
    Logger LOGGER;

    @ConfigProperty(name = "app.certificate.path", defaultValue = "/var/data/qiot/client.ts")
    String dataFilePathString;

    @Inject
    @RestClient
    RegistrationServiceClient registrationClient;

    @Inject
    StationService stationService;

    public RegisterBean register(String serial, String name, double longitude,
            double latitude) {
        RegisterBean registerBean=new RegisterBean();
        try {
            Response response = registrationClient.register(serial, name,
                    longitude, latitude);
            MultipartFormDataInput formData = response
                    .readEntity(MultipartFormDataInput.class);

            registerBean.stationId = formData.getFormDataPart("id", String.class, null);
            LOGGER.debug("Acquired stationID: {}", registerBean.stationId);
            InputStream trustStoreIS = formData.getFormDataPart("ts",
                    InputStream.class, null);
            LOGGER.debug("Acquired certificate IS: {}", trustStoreIS);
            registerBean.trustStorePassword = formData.getFormDataPart("tspass",
                    String.class, null);
            LOGGER.debug("Acquired trust store password: {}",
                    registerBean.trustStorePassword);

            writeToFile(trustStoreIS);
            updateConfig();
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred registering the device to the data hub.",
                    e);
        }
        return registerBean;
    }

    private void updateConfig() {
        

    }

    private void writeToFile(InputStream trustStoreIS) throws IOException {
        Path truststore = Paths.get(dataFilePathString);
        Files.createFile(truststore);
        byte[] buffer = trustStoreIS.readAllBytes();
        try (OutputStream outputStream = Files.newOutputStream(truststore);) {
            outputStream.write(buffer);
        }
    }
    
    
}
