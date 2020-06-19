package com.redhat.qiot.edge.service.location;


import com.redhat.qiot.edge.domain.CoordinatesBean;


public interface OpenStreetMapService {

    CoordinatesBean getCoordinates(String address)
            throws Exception;

}