/**
 * 
 */
package com.redhat.qiot.edge.service.edge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.redhat.qiot.edge.domain.CoordinatesBean;
import com.redhat.qiot.edge.util.events.CoordinatesFound;
import com.redhat.qiot.edge.util.events.RegistrationSuccessful;
import com.redhat.qiot.edge.util.events.SerialIDCollected;

/**
 * @author Andrea
 *
 */
@ApplicationScoped
class StationServiceImpl implements StationService {

    private int stationId;

    private String stationSerial;

    private CoordinatesBean coordinates;

    @Override
    public int getStationId() {
	return stationId;
    }

    void setStationId(@Observes @RegistrationSuccessful Integer stationId) {
	this.stationId = stationId;
    }

    /**
     * @return the stationSerial
     */
    public String getStationSerial() {
	return stationSerial;
    }

    /**
     * @param stationSerial the stationSerial to set
     */
    void setStationSerial(@Observes @SerialIDCollected String stationSerial) {
	this.stationSerial = stationSerial;
    }

    /**
     * @return the coordinates
     */
    public CoordinatesBean getCoordinates() {
	return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    void setCoordinates(@Observes @CoordinatesFound CoordinatesBean coordinates) {
	this.coordinates = coordinates;
    }

}
