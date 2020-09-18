package com.redhat.qiot.edge.service.location;


import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.slf4j.Logger;

import com.redhat.qiot.edge.domain.CoordinatesBean;


@Singleton
public class OpenStreetMapServiceImpl
        implements OpenStreetMapService {

    @Inject
    Logger LOGGER;

    @Override
    public CoordinatesBean getCoordinates(String address)
            throws Exception {
        CoordinatesBean coordinates = null;
        StringBuffer query = null;
        String[] split = null;

        split = address.split(" ");
        query = new StringBuffer();
        query.append("https://nominatim.openstreetmap.org/search?q=");

        if (split.length == 0) {
            return null;
        }

        for (int i = 0; i < split.length; i++) {
            query.append(split[i]);
            if (i < (split.length - 1)) {
                query.append("+");
            }
        }
        query.append("&format=json&addressdetails=1");

        LOGGER.debug("Query:" + query);

        URL url = new URL(query.toString());
        try (InputStream is = url.openStream();
                JsonReader reader = Json.createReader(is)) {
            JsonArray jsonArray = reader.readArray();
            LOGGER.debug(jsonArray.toString());
            JsonObject jsonObject = jsonArray.getJsonObject(0);
            LOGGER.debug(jsonObject.toString());
            coordinates = new CoordinatesBean();
            coordinates.longitude = Double
                    .parseDouble(jsonObject.getString("lon"));
            coordinates.latitude = Double
                    .parseDouble(jsonObject.getString("lat"));
            LOGGER.debug(coordinates.toString());
        }

        return coordinates;
    }
}
