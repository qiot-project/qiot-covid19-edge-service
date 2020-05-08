package com.redhat.qiot.service.sensor.queries;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.redhat.qiot.domain.SensorBean;

abstract class AbstractSensorQuery implements SensorQuery {

    @Inject
    Logger LOGGER;

    @Inject
    RestClientProducer restClientProducer;

    private WebTarget target;

    // TODO: must be injected from application properties!
    final String BASIC_URI = "http://192.168.178.53:5000";

    final String URI_STRING;

    AbstractSensorQuery() {
	super();
	URI_STRING = getUriString();
    }
    
    abstract String getUriString();

    @Override
    public SensorBean querySensor() {
//	"http://192.168.178.53:5000/weather/compensatedtemperature"
	Client client = restClientProducer.getClient();
	target = client.target(URI_STRING);
	try (Response response = target.request().get()) {

	    if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	    }
	    response.bufferEntity();
	    LOGGER.debug("RAW Server response : \n");
	    LOGGER.debug(response.readEntity(String.class));
	    return response.readEntity(SensorBean.class);
	}

    }

}
