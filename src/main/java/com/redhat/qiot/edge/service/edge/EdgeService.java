/**
 * 
 */
package com.redhat.qiot.edge.service.edge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.redhat.qiot.edge.domain.StationDataBean;
import com.redhat.qiot.edge.service.sensor.SensorServiceClient;
import com.redhat.qiot.edge.service.station.StationService;
import com.redhat.qiot.edge.service.telemetry.MqttEndpointClient;
import com.redhat.qiot.edge.util.decorator.MeasurementDecorator;

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
    MeasurementDecorator measurementDecorator;

    // private StationDataBean stationData;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...{}");
        // stationData =
        stationService.checkRegistration();
    }

//    @Scheduled(every = "10s")
    void run() {
        String measurement = null;
        String decoratedMeasurement = null;
        try {
            measurement = sensorServiceClient.getGasMeasurement();
            decoratedMeasurement = measurementDecorator
                    .decorate(stationService.getStationId(), measurement);
            LOGGER.info("Collected GAS measurement: {}", decoratedMeasurement);
            telemetryEndpointClient.sendGas(decoratedMeasurement);
        } catch (Exception e) {
            LOGGER.error("An error occurred retrieving GAS maeasurement", e);
        }
        try {
            measurement = sensorServiceClient.getParticulatesMeasurement();
            decoratedMeasurement = measurementDecorator
                    .decorate(stationService.getStationId(), measurement);
            LOGGER.info("Collected PARTICULA measurement: {}",
                    decoratedMeasurement);
            telemetryEndpointClient.sendPollution(decoratedMeasurement);
        } catch (Exception e) {
            LOGGER.error(
                    "An error occurred retrieving Particulates maeasurement",
                    e);
        }
    }
}
