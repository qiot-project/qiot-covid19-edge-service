/**
 * 
 */
package com.redhat.qiot.edge.service.edge;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.qiot.edge.domain.CoordinatesBean;
import com.redhat.qiot.edge.service.datahub.DataHubClientService;
import com.redhat.qiot.edge.service.datahub.MQTTDataHubCollectionClientService;
import com.redhat.qiot.edge.service.location.OpenStreetMapService;
import com.redhat.qiot.edge.service.sensor.SensorClientService;
import com.redhat.qiot.edge.util.decorator.MeasurementDecorator;
import com.redhat.qiot.edge.util.events.CoordinatesFound;
import com.redhat.qiot.edge.util.events.RegistrationSuccessful;
import com.redhat.qiot.edge.util.events.SerialIDCollected;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

/**
 * @author abattagl
 *
 */
@Startup
@ApplicationScoped
class EdgeServiceImpl implements EdgeService {

    @Inject
    Logger LOGGER;

    @Inject
    ObjectMapper mapper;

    @Inject
    @RestClient
    DataHubClientService dataHubClientService;

    @Inject
    MQTTDataHubCollectionClientService mqttDataCollectionClientService;

    @Inject
    @RestClient
    SensorClientService sensorClientService;

    @Inject
    StationService stationIdService;

    @Inject
    OpenStreetMapService locationService;

    @Inject
    MeasurementDecorator measurementDecorator;

    @Inject
    @RegistrationSuccessful
    Event<Integer> registrationSuccessfulEvent;

    @Inject
    @SerialIDCollected
    Event<String> serialIDCollectedEvent;

    @Inject
    @CoordinatesFound
    Event<CoordinatesBean> coordinatesFoundEvent;

    @ConfigProperty(name = "station.serial")
    String STATION_SERIAL;
    @ConfigProperty(name = "station.address")
    String STATION_ADDRESS;
    @ConfigProperty(name = "station.name")
    String STATION_NAME;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...{}");
    }

    @PostConstruct
    void init() {
        try {
            String stationSerial = STATION_SERIAL;
            if(Objects.isNull(STATION_SERIAL))
                stationSerial = sensorClientService.getStationId();
            
            serialIDCollectedEvent.fire(stationSerial);
            LOGGER.info("Station serial ID is {}", stationSerial);
            CoordinatesBean coordinatesBean = locationService
                    .getCoordinates(STATION_ADDRESS);
            coordinatesFoundEvent.fire(coordinatesBean);
            LOGGER.info("Station coordinates are {}", coordinatesBean);

            JsonObject jsonObject = null;
            JsonObjectBuilder jsonObjectBuilder = null;
            String jsonString = null;

            jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("serial", stationSerial).add("name", STATION_NAME)
                    .add("lon", coordinatesBean.longitude)
                    .add("lat", coordinatesBean.latitude);
            jsonObject = jsonObjectBuilder.build();
            jsonObjectBuilder = null;
            jsonString = jsonObject.toString();
            jsonObject = null;

            int stationId = Integer
                    .parseInt(dataHubClientService.register(jsonString));
            LOGGER.info("Station ID is {}", stationId);

            registrationSuccessfulEvent.fire(stationId);
        } catch (Exception e) {
            LOGGER.error("Cannot register to the server", e);
        }
    }

    @Scheduled(every = "3s")
    void run() {
        String measurement = null;
        String decoratedMeasurement = null;
        try {
            measurement = sensorClientService.getGasMeasurement();
            decoratedMeasurement = measurementDecorator.decorate(measurement);
            LOGGER.info("Collected GAS measurement: {}", decoratedMeasurement);
            mqttDataCollectionClientService.sendGas(decoratedMeasurement);
        } catch (Exception e) {
            LOGGER.error("An error occurred retrieving GAS maeasurement", e);
        }
        try {
            measurement = sensorClientService.getParticulatesMeasurement();
            decoratedMeasurement = measurementDecorator.decorate(measurement);
            LOGGER.info("Collected PARTICULA measurement: {}",
                    decoratedMeasurement);
            mqttDataCollectionClientService.sendPollution(decoratedMeasurement);
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred retrieving Particulates maeasurement",
                    e);
        }
    }

    @PreDestroy
    void destroy() {
        try {
            LOGGER.info("Unregistering thet measurement station.");
            dataHubClientService.unregister(
                    stationIdService.getStationId());
        } catch (Exception e) {
            LOGGER.error("An error occurred unregistering the station", e);
        }
    }
}
