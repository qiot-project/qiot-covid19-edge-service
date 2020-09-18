/**
 * 
 */
package com.redhat.qiot.edge.service.edge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * @author Andrea
 *
 */
@ApplicationScoped
class MeasurementStationServiceImpl implements MeasurementStationService {

    @Inject
    Logger LOGGER;

    @Inject
    RegistrationService registrationService;

    private int stationId;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        registrationService.register();
    }

    // @PostConstruct
    // void init() {
    // registrationService.register();
    // }

    @Override
    public int getStationId() {
        return stationId;
    }

    // @PreDestroy
    // void destroy() {
    // try {
    // LOGGER.info("Unregistering thet measurement station.");
    // registrationService.unregister();
    // } catch (Exception e) {
    // LOGGER.error("An error occurred unregistering the station", e);
    // }
    // }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        registrationService.unregister();
    }

}
