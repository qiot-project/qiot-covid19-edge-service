/**
 * 
 */
package io.qiot.covid19.edge.util.decorator;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.qiot.covid19.edge.util.exception.DataDecorationException;

/**
 * @author Andrea
 *
 */
@Singleton
public class BaseTelemetryDecorator implements TelemetryDecorator {

    private static final String INSTANT = "instant";

    private static final String STATION_ID = "stationId";

    @Inject
    Logger LOGGER;

    @Inject
    ObjectMapper MAPPER;

    @Override
    public String decorate(String stationId, String telemetry)
            throws DataDecorationException {
        try {
            ObjectNode telemetryNode = (ObjectNode) MAPPER.readTree(telemetry);
            ObjectNode rootNode = MAPPER.createObjectNode();
            rootNode.put(STATION_ID, stationId);
            rootNode.put(INSTANT,
                    OffsetDateTime.now(ZoneOffset.UTC).toInstant().toString());
            rootNode.setAll(telemetryNode);
            return rootNode.toString();
        } catch (JsonProcessingException e) {
            throw new DataDecorationException(e);
        }

        // JsonObject mJsonObject = null;
        // JsonObject finalJsonObject = null;
        // JsonObjectBuilder job = null;
        // String decoratedTelemetry = null;
        // try (JsonReader reader = Json
        // .createReader(new StringReader(telemetry));) {
        // mJsonObject = reader.readObject();
        // }
        // job = Json.createObjectBuilder();
        // job.add(STATION_ID, stationId)//
        // .add(INSTANT, OffsetDateTime.now(ZoneOffset.UTC).toInstant()
        // .toString());
        // for (Entry<String, JsonValue> entry : mJsonObject.entrySet()) {
        // job.add(entry.getKey(), entry.getValue());
        // }
        // mJsonObject = null;
        // finalJsonObject = job.build();
        // job = null;
        // decoratedTelemetry = finalJsonObject.toString();
        // finalJsonObject = null;
        // return decoratedTelemetry;
    }

}
