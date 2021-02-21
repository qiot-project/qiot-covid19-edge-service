/**
 * 
 */
package io.qiot.covid19.edge.service.station;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.covid19.edge.domain.CoordinatesBean;
import io.qiot.covid19.edge.domain.RegisterBean;
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

    @ConfigProperty(name = "app.datafile.path")
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

    @ConfigProperty(name = "app.station.serial")
    String STATION_SERIAL;
    @ConfigProperty(name = "app.station.address")
    String STATION_ADDRESS;
    @ConfigProperty(name = "app.station.name")
    String STATION_NAME;

    private StationDataBean stationData;

    // void onStart(@Observes StartupEvent ev) {
    // LOGGER.info("Init {}", this.getClass());
    // stationSerial = "stationserial";
    // coordinates = new CoordinatesBean();
    // coordinates.longitude = 0;
    // coordinates.latitude = 0;
    // }

    public StationDataBean checkRegistration() throws DataValidationException {
        Path dataFilePath = Paths.get(dataFilePathString);
        if (Files.exists(dataFilePath)) {
            LOGGER.info(
                    "Device is already registered. Loading data from persistent volume...");
            try {
                String datafileContent = Files.readString(dataFilePath);
                // try (Jsonb jsonb = JsonbBuilder.create();) {
                // stationData = jsonb.fromJson(datafileContent,
                // StationDataBean.class);
                // }
                stationData = MAPPER.readValue(datafileContent,
                        StationDataBean.class);
                // try (StringReader sr = new StringReader(datafileContent);
                // JsonReader reader = Json.createReader(sr)) {
                // stationData = new StationDataBean();
                // JsonObject jsonObject = reader.readObject();
                // stationData.id = jsonObject.getString("id");
                // stationData.serial = jsonObject.getString("serial");
                // stationData.name = jsonObject.getString("name");
                // CoordinatesBean coordinates = new CoordinatesBean();
                // JsonObject coordinatesJsonObject = jsonObject
                // .getJsonObject("coordinates");
                // coordinates = new CoordinatesBean();
                // coordinates.longitude = coordinatesJsonObject
                // .getJsonNumber("longitude").doubleValue();
                // coordinates.latitude = coordinatesJsonObject
                // .getJsonNumber("latitude").doubleValue();
                // stationData.coordinates = coordinates;
                // coordinates = null;
                // }
            } catch (Exception e) {
                LOGGER.error("An error occurred loading the station data file.",
                        e);
                throw new DataValidationException(e);
            }
            LOGGER.info("Data loaded successfully: {}", stationData);
            // TODO: check cert validity
        } else {
            LOGGER.info(
                    "Device is not registered. Stepping through the registration process...");

            stationData = new StationDataBean();
            try {
                if (STATION_SERIAL.equals("empty"))
                    stationData.serial = sensorServiceClient.getSerialId()
                            .getString("id");
                else
                    stationData.serial = STATION_SERIAL;
                stationData.name = STATION_NAME;
                stationData.coordinates = localizationService
                        .getCoordinates(STATION_ADDRESS);
                RegisterBean registerBean = registrationService.register(
                        stationData.serial, stationData.name,
                        stationData.coordinates.longitude,
                        stationData.coordinates.latitude);
                LOGGER.info("Raw registration info: {}", registerBean);
                stationData.id = registerBean.stationId;
                stationData.tspass = registerBean.trustStorePassword;
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
        return stationData.tspass;
    }

}
