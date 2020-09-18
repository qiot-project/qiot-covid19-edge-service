/**
 * 
 */
package com.redhat.qiot.edge.service.edge;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.redhat.qiot.edge.domain.CoordinatesBean;
import com.redhat.qiot.edge.service.datahub.DataHubClientService;
import com.redhat.qiot.edge.service.location.OpenStreetMapService;
import com.redhat.qiot.edge.service.sensor.SensorClientService;

/**
 * @author Andrea
 *
 */
@ApplicationScoped
class RegistrationServiceImpl implements RegistrationService {

    @Inject
    Logger LOGGER;

    @Inject
    @RestClient
    DataHubClientService dataHubClientService;

    @Inject
    @RestClient
    SensorClientService sensorClientService;

    @Inject
    OpenStreetMapService locationService;

    private int stationId;

    private String stationSerial;

    private CoordinatesBean coordinatesBean;

    @ConfigProperty(name = "station.serial")
    String STATION_SERIAL;
    @ConfigProperty(name = "station.address")
    String STATION_ADDRESS;
    @ConfigProperty(name = "station.name")
    String STATION_NAME;

    @Override
    public int register() {
        try {
            try {
                stationSerial = sensorClientService.getStationId();
            } catch (Exception e) {
            }

            if (Objects.isNull(stationSerial))
                stationSerial = STATION_SERIAL;

            LOGGER.info("Station serial ID is {}", stationSerial);
             coordinatesBean = locationService
                    .getCoordinates(STATION_ADDRESS);
            LOGGER.info("Station coordinates are {}", coordinatesBean);

            // JsonObject jsonObject = null;
            // JsonObjectBuilder jsonObjectBuilder = null;
            // String jsonString = null;
            //
            // jsonObjectBuilder = Json.createObjectBuilder();
            // jsonObjectBuilder.add("serial", stationSerial)
            // .add("name", STATION_NAME)
            // .add("lon", coordinatesBean.longitude)
            // .add("lat", coordinatesBean.latitude);
            // jsonObject = jsonObjectBuilder.build();
            // jsonObjectBuilder = null;
            // jsonString = jsonObject.toString();
            // jsonObject = null;

            stationId = Integer.parseInt(dataHubClientService.register(
                    stationSerial, STATION_NAME, coordinatesBean.longitude,
                    coordinatesBean.latitude));
            LOGGER.info("Station ID is {}", stationId);

        } catch (Exception e) {
            LOGGER.error("Cannot register to the server", e);
        }
        return stationId;
    }

    @Override
    public void unregister() {
        try {
            LOGGER.info("Unregistering thet measurement station.");
            dataHubClientService.unregister(stationId);
        } catch (Exception e) {
            LOGGER.error("An error occurred unregistering the station", e);
        }
    }

}
