/**
 * 
 */
package io.qiot.covid19.edge.service.edge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import io.qiot.covid19.edge.domain.StationDataBean;
import io.qiot.covid19.edge.service.sensor.SensorServiceClient;
import io.qiot.covid19.edge.service.station.StationService;
import io.qiot.covid19.edge.service.telemetry.MqttEndpointClient;
import io.qiot.covid19.edge.util.decorator.TelemetryDecorator;
import io.qiot.covid19.edge.util.exception.DataValidationException;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

/**
 * @author abattagl
 *
 */
@Startup
@ApplicationScoped
public class EdgeService {

    @Inject
    Logger LOGGER;

    @Inject
    MqttEndpointClient telemetryEndpointClient;

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
    }

    @Scheduled(every = "3s", delayed = "5s")
    void run() {
        String telemetry = null;
        String enrichedTelemetry = null;
        try {
            telemetry = sensorServiceClient.getGasMeasurement();
            enrichedTelemetry = measurementDecorator
                    .decorate(stationService.getStationId(), telemetry);
            LOGGER.info("Collected GAS measurement: {}", enrichedTelemetry);
            telemetryEndpointClient.sendGas(enrichedTelemetry);
        } catch (Exception e) {
            LOGGER.error("An error occurred retrieving GAS maeasurement", e);
        }
        try {
            telemetry = sensorServiceClient.getParticulatesMeasurement();
            enrichedTelemetry = measurementDecorator
                    .decorate(stationService.getStationId(), telemetry);
            LOGGER.info("Collected PARTICULA measurement: {}",
                    enrichedTelemetry);
            telemetryEndpointClient.sendPollution(enrichedTelemetry);
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred retrieving Particulates maeasurement",
                    e);
        }
    }
}
