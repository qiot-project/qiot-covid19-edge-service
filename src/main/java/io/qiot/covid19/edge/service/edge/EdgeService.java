/**
 * 
 */
package io.qiot.covid19.edge.service.edge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import io.qiot.covid19.edge.service.sensor.SensorServiceClient;
import io.qiot.covid19.edge.service.station.StationService;
import io.qiot.covid19.edge.service.telemetry.TelemetryService;
import io.qiot.covid19.edge.util.decorator.TelemetryDecorator;
import io.qiot.covid19.edge.util.exception.DataDecorationException;
import io.qiot.covid19.edge.util.exception.DataValidationException;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.vertx.mqtt.MqttException;

/**
 * @author andreabattaglia
 *
 */
@Startup
@ApplicationScoped
public class EdgeService {

    @Inject
    Logger LOGGER;

    @Inject
    TelemetryService telemetryService;

    @Inject
    StationService stationService;

    @Inject
    @RestClient
    SensorServiceClient sensorServiceClient;

    @Inject
    TelemetryDecorator measurementDecorator;

    // private StationDataBean stationData;

    void onStart(@Observes StartupEvent ev) throws DataValidationException {
        LOGGER.info("The application is starting...{}");
        // stationData =
        stationService.checkRegistration();
        telemetryService.getReady();
    }

    @Scheduled(every = "5s", delayed = "5s")
    void gasTelemetry() {
        String telemetry = null;
        String enrichedTelemetry = null;
        try {
            telemetry = sensorServiceClient.getGasMeasurement();
            LOGGER.info("Collected GAS telemetry: {}", telemetry);
        } catch (Exception e1) {
            LOGGER.error(
                    "An error occurred collecting GAS telemetry from the sensor service",
                    e1);
            return;
        }
        try {
            enrichedTelemetry = measurementDecorator
                    .decorate(stationService.getStationId(), telemetry);
            LOGGER.info("Enriched GAS telemetry: {}", enrichedTelemetry);
        } catch (DataDecorationException e1) {
            LOGGER.error("An error occurred enreaching GAS telemetry", e1);
            return;
        }
            telemetryService.sendGas(enrichedTelemetry);
    }

    @Scheduled(every = "5s", delayed = "7s")
    void pollutionTelemetry() {
        String telemetry = null;
        String enrichedTelemetry = null;
        try {
            telemetry = sensorServiceClient.getParticulatesMeasurement();
        } catch (Exception e1) {
            LOGGER.error(
                    "An error occurred collecting POLLUTION telemetry from the sensor service",
                    e1);
            return;
        }
        try {
            enrichedTelemetry = measurementDecorator
                    .decorate(stationService.getStationId(), telemetry);
            LOGGER.info("Enriched GAS telemetry: {}", enrichedTelemetry);
        } catch (DataDecorationException e1) {
            LOGGER.error("An error occurred enreaching POLLUTION telemetry",
                    e1);
            return;
        }
        try {
            telemetryService.sendPollution(enrichedTelemetry);
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred producing the POLLUTION telemetry event",
                    e);
            return;
        }
    }
}
