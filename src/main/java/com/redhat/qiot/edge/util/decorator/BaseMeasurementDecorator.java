/**
 * 
 */
package com.redhat.qiot.edge.util.decorator;

import java.io.StringReader;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.slf4j.Logger;

import com.redhat.qiot.edge.service.station.StationService;

/**
 * @author Andrea
 *
 */
@Singleton
public class BaseMeasurementDecorator implements MeasurementDecorator {

    @Inject
    Logger LOGGER;

    @Override
    public String decorate(String stationId, String measurement) {
        JsonObject mJsonObject = null;
        JsonObject finalJsonObject = null;
        JsonObjectBuilder job = null;
        String decoratedMeasurement = null;
        try (JsonReader reader = Json
                .createReader(new StringReader(measurement));) {
            mJsonObject = reader.readObject();
        }
        job = Json.createObjectBuilder();
        job.add("stationId", stationId)//
                .add("instant", OffsetDateTime.now(ZoneOffset.UTC).toInstant()
                        .toString());
        for (Entry<String, JsonValue> entry : mJsonObject.entrySet()) {
            job.add(entry.getKey(), entry.getValue());
        }
        mJsonObject = null;
        finalJsonObject = job.build();
        job = null;
        decoratedMeasurement = finalJsonObject.toString();
        finalJsonObject = null;
        return decoratedMeasurement;
    }

}
