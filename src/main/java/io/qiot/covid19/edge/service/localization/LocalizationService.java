package io.qiot.covid19.edge.service.localization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.covid19.edge.domain.CoordinatesBean;

@ApplicationScoped
public class LocalizationService {

    @Inject
    Logger LOGGER;

    @Inject
    ObjectMapper MAPPER;

    @Inject
    @RestClient
    NominatimServiceClient nominatimServiceClient;

    public CoordinatesBean getCoordinates(String address) throws Exception {
        CoordinatesBean coordinates = null;

        String jsonResult = nominatimServiceClient.query(address, "json", 1,
                "en");

        JsonNode arrayNode = MAPPER.readTree(jsonResult);
        LOGGER.info(arrayNode.toString());
        JsonNode objectNode = arrayNode.get(0);
        LOGGER.info(objectNode.toString());

        coordinates = new CoordinatesBean();

        coordinates.longitude = objectNode.path("lon").asDouble();
        coordinates.latitude = objectNode.path("lat").asDouble();

        LOGGER.info(coordinates.toString());

        return coordinates;
    }
}
