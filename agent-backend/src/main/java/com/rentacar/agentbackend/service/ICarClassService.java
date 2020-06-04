package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreateCarClassRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarClassRequest;
import com.rentacar.agentbackend.dto.response.CarClassResponse;

import java.util.List;
import java.util.UUID;

public interface ICarClassService {

    CarClassResponse createCarClass(CreateCarClassRequest request) throws Exception;

    CarClassResponse updateCarClass(UpdateCarClassRequest request, UUID id) throws Exception;

    void deleteCarClass(UUID id) throws Exception;

    CarClassResponse getCarClass(UUID id) throws Exception;

    List<CarClassResponse> getAllCarClasses() throws Exception;
}
