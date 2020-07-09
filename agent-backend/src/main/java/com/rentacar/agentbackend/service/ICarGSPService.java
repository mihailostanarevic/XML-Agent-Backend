package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.TrackCarRequest;
import com.rentacar.agentbackend.dto.response.CarGpsResponse;

import java.util.List;
import java.util.UUID;

public interface ICarGSPService {

    CarGpsResponse trackCar(TrackCarRequest request) throws Exception;

    List<CarGpsResponse> getAllCarsWhichCanBeTrackedByAgent(UUID id) throws Exception;

    List<CarGpsResponse> getAllTrackedCarsByAgent(UUID id) throws Exception;

    void tracking() throws Exception;
}
