package io.qiot.covid19.edge.service.localization;

import java.io.StringReader;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import io.qiot.covid19.edge.domain.CoordinatesBean;

@ApplicationScoped
public class LocalizationService {

    @Inject
    Logger LOGGER;

    @Inject
    @RestClient
    NominatimServiceClient nominatimServiceClient;

    public CoordinatesBean getCoordinates(String address) throws Exception {
        CoordinatesBean coordinates = null;

        String jsonResult = nominatimServiceClient.query(address, "json", 1);

        try (StringReader sr = new StringReader(jsonResult);
                JsonReader reader = Json.createReader(sr)) {
            JsonArray jsonArray = reader.readArray();
            LOGGER.info(jsonArray.toString());
            JsonObject jsonObject = jsonArray.getJsonObject(0);
            LOGGER.info(jsonObject.toString());
            coordinates = new CoordinatesBean();
            coordinates.longitude = Double
                    .parseDouble(jsonObject.getString("lon"));
            coordinates.latitude = Double
                    .parseDouble(jsonObject.getString("lat"));
            LOGGER.info(coordinates.toString());
        }

        return coordinates;
    }
}
