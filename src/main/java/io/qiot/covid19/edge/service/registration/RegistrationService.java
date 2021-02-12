package io.qiot.covid19.edge.service.registration;

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

import io.qiot.covid19.edge.domain.RegisterBean;
import io.qiot.covid19.edge.service.station.StationService;
import io.qiot.covid19.edge.util.exception.RegistrationException;

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
            double latitude) throws RegistrationException {
        RegisterBean registerBean=new RegisterBean();
        try {
            Response response = registrationClient.register(serial, name,
                    longitude, latitude);
            MultipartFormDataInput formData = response
                    .readEntity(MultipartFormDataInput.class);

            registerBean.stationId = formData.getFormDataPart("id", String.class, null);
            LOGGER.info("Acquired stationID: {}", registerBean.stationId);
            InputStream trustStoreIS = formData.getFormDataPart("ts",
                    InputStream.class, null);
            LOGGER.info("Acquired certificate IS: {}", trustStoreIS);
            registerBean.trustStorePassword = formData.getFormDataPart("tspass",
                    String.class, null);
            LOGGER.info("Acquired trust store password: {}",
                    registerBean.trustStorePassword);

            writeToFile(trustStoreIS);
            updateConfig();
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred registering the device to the data hub.",
                    e);
            throw new RegistrationException(e);
        }
        return registerBean;
    }

    private void updateConfig() {
        

    }

    private void writeToFile(InputStream trustStoreIS) throws IOException {
        Path truststore = Paths.get(dataFilePathString);
        Files.createFile(truststore);
        byte[] buffer = trustStoreIS.readAllBytes();
        //TODO: transform PEM into TRUST-STORE
        try (OutputStream outputStream = Files.newOutputStream(truststore);) {
            outputStream.write(buffer);
        }
    }
    
    
}
