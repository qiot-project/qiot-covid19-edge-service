/**
 * 
 */
package com.redhat.qiot.edge.service.station;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.redhat.qiot.edge.domain.CoordinatesBean;
import com.redhat.qiot.edge.domain.RegisterBean;
import com.redhat.qiot.edge.domain.StationDataBean;
import com.redhat.qiot.edge.service.localization.LocalizationService;
import com.redhat.qiot.edge.service.registration.RegistrationService;
import com.redhat.qiot.edge.service.sensor.SensorServiceClient;

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

    public StationDataBean checkRegistration() {
        Path dataFilePath = Paths.get(dataFilePathString);
        if (Files.exists(dataFilePath)) {
            try {
                String datafileContent = Files.readString(dataFilePath);
                try (Jsonb jsonb = JsonbBuilder.create();) {
                    stationData = jsonb.fromJson(datafileContent,
                            StationDataBean.class);
                }
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
            }
            // TODO: check cert validity
        } else {
            stationData=new StationDataBean();
            try (Jsonb jsonb = JsonbBuilder.create();) {
                if (Objects.isNull(STATION_SERIAL))
                    stationData.serial = sensorServiceClient.getSerialId();
                else
                    stationData.serial = STATION_SERIAL;
                stationData.name = STATION_NAME;
                stationData.coordinates = localizationService
                        .getCoordinates(STATION_ADDRESS);
                RegisterBean registerBean = registrationService.register(
                        stationData.serial, stationData.name,
                        stationData.coordinates.longitude,
                        stationData.coordinates.latitude);
                stationData.id = registerBean.stationId;
                stationData.tspass = registerBean.trustStorePassword;
                Files.createFile(dataFilePath);

                String stationDataString = jsonb.toJson(stationData);
                Files.writeString(dataFilePath, stationDataString);
            } catch (Exception e) {
                LOGGER.error(
                        "An error occurred registering the measurement station.",
                        e);
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
