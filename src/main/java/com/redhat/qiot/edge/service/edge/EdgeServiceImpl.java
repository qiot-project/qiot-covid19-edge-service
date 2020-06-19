/**
 * 
 */
package com.redhat.qiot.edge.service.edge;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;


import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.qiot.edge.domain.CoordinatesBean;
import com.redhat.qiot.edge.service.datahub.MQTTDataHubCollectionClientService;
import com.redhat.qiot.edge.service.location.OpenStreetMapService;
import com.redhat.qiot.edge.service.sensor.SensorClientService;
import com.redhat.qiot.edge.util.decorator.MeasurementDecorator;
import com.redhat.qiot.edge.util.events.RegistrationSuccessful;


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

    //    @Inject
    //    @RestClient
    //    DataHubClientService dataHubClientService;

    @Inject
    MQTTDataHubCollectionClientService dataHubClientService;

    @Inject
    @RestClient
    SensorClientService sensorClientService;

    @Inject
    StationIdService stationIdService;

    @Inject
    OpenStreetMapService locationService;

    @Inject
    MeasurementDecorator measurementDecorator;

    @Inject
    @RegistrationSuccessful
    Event<Integer> registrationSuccessfulEvent;

    @ConfigProperty(name = "station.address")
    String address;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...{}");
    }

    @PostConstruct
    void init() {
        try {
            //TODO:complete station registration process
            String stationSerial = sensorClientService.getStationId();
            LOGGER.info("Station serial ID is {}", stationSerial);
            CoordinatesBean coordinatesBean = locationService
                    .getCoordinates(address);
            LOGGER.info("Station coordinates are {}",
                    coordinatesBean);
            //            int stationId = dataHubClientService.register("");

            registrationSuccessfulEvent.fire(0);
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
            decoratedMeasurement = measurementDecorator
                    .decorate(measurement);
            LOGGER.info("Gas measurement: {}", decoratedMeasurement);
            dataHubClientService.sendGas(decoratedMeasurement);
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred retrieving GAS maeasurement");
        }
        try {
            measurement = sensorClientService
                    .getParticulatesMeasurement();
            decoratedMeasurement = measurementDecorator
                    .decorate(measurement);
            LOGGER.info("Particulates measurement: {}",
                    decoratedMeasurement);
            dataHubClientService.sendPollution(decoratedMeasurement);
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred retrieving Particulates maeasurement");
        }
    }

    @PreDestroy
    void destroy() {
        try {
            //            dataHubClientService
            //                    .unregister(stationIdService.getStationId());
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred unregistering the station", e);
        }
    }
}
