/**
 * 
 */
package com.redhat.qiot.service.sensor.queries;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.slf4j.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * @author abattagl
 *
 */
@ApplicationScoped
//@Startup
class RestClientProducer {

    @Inject
    Logger LOGGER;

    private Client client;

    void onStart(@Observes StartupEvent ev) {
	client = ClientBuilder.newClient();
    }

    public Client getClient() {
	return client;
    }

    void onStop(@Observes ShutdownEvent ev) {
	client.close();
    }
}
