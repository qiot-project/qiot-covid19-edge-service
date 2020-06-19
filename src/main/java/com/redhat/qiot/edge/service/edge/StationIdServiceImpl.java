/**
 * 
 */
package com.redhat.qiot.edge.service.edge;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;


import com.redhat.qiot.edge.util.events.RegistrationSuccessful;


/**
 * @author Andrea
 *
 */
@ApplicationScoped
class StationIdServiceImpl implements StationIdService {

    private int stationId;

    @Override
    public int getStationId() {
        return stationId;
    }

    void setStationId(
            @Observes @RegistrationSuccessful Integer stationId) {
        this.stationId = stationId;
    }
}
