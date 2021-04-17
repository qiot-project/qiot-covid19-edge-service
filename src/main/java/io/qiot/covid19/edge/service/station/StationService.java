/**
 * 
 */
package io.qiot.covid19.edge.service.station;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.covid19.edge.domain.CoordinatesBean;
import io.qiot.covid19.edge.domain.StationDataBean;
import io.qiot.covid19.edge.service.localization.LocalizationService;
import io.qiot.covid19.edge.service.registration.RegistrationService;
import io.qiot.covid19.edge.service.sensor.SensorServiceClient;
import io.qiot.covid19.edge.util.exception.DataValidationException;

/**
 * @author Andrea
 *
 */
@ApplicationScoped
public class StationService {
    @Inject
    Logger LOGGER;

    @ConfigProperty(name = "qiot.datafile.path")
    String dataFilePathString;

    @Inject
    ObjectMapper MAPPER;

    @Inject
    @RestClient
    SensorServiceClient sensorServiceClient;

    @Inject
    RegistrationService registrationService;

    @Inject
    LocalizationService localizationService;

    @ConfigProperty(name = "qiot.station.serial")
    String STATION_SERIAL;
    @ConfigProperty(name = "qiot.station.address")
    String STATION_ADDRESS;
    @ConfigProperty(name = "qiot.station.name")
    String STATION_NAME;

    @ConfigProperty(name = "qiot.mqtts.ks.password")
    String ksPassword;
    @ConfigProperty(name = "qiot.mqtts.ts.password")
    String tsPassword;

    private StationDataBean stationData;

    public StationDataBean checkRegistration() throws DataValidationException {
        Path dataFilePath = Paths.get(dataFilePathString);
        if (Files.exists(dataFilePath)) {
            LOGGER.info(
                    "Device is already registered. Loading data from persistent volume...");
            try {
                String datafileContent = Files.readString(dataFilePath);
                stationData = MAPPER.readValue(datafileContent,
                        StationDataBean.class);
            } catch (Exception e) {
                LOGGER.error("An error occurred loading the station data file.",
                        e);
                throw new DataValidationException(e);
            }
            LOGGER.info("Data loaded successfully: {}", stationData);
        } else {
            LOGGER.info(
                    "Device is not registered. Stepping through the registration process...");

            stationData = new StationDataBean();
            try {
                if (STATION_SERIAL.equals("empty"))
                    stationData.serial = sensorServiceClient.getSerialId().id;
                else
                    stationData.serial = STATION_SERIAL;
                stationData.name = STATION_NAME;
                stationData.coordinates = localizationService
                        .getCoordinates(STATION_ADDRESS);
                String stationId = registrationService.register(
                        stationData.serial, stationData.name,
                        stationData.coordinates.longitude,
                        stationData.coordinates.latitude, ksPassword); 

                LOGGER.info("Received station ID: {}", stationId);
                stationData.id = stationId;
                Files.createFile(dataFilePath);

                String stationDataString = MAPPER
                        .writeValueAsString(stationData);
                Files.writeString(dataFilePath, stationDataString);

                LOGGER.info("Data Created successfully: {}", stationData);
            } catch (Exception e) {
                LOGGER.error(
                        "An error occurred registering the measurement station.",
                        e);
                throw new DataValidationException(e);
            }
        }
        return stationData;
    }

    public String getStationId() {
        return stationData.id;
    }

    public String getStationSerial() {
        return stationData.serial;
    }

    public String getStationName() {
        return stationData.name;
    }

    public CoordinatesBean getStationCoordinates() {
        return stationData.coordinates;
    }

    public String getTrustStorePassword() {
        return tsPassword;
    }

    public String getKeyStorePassword() {
        return ksPassword;
    }

}
