package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.CarAccessoryResponse;

import java.util.List;

public interface ICarAccessoriesService {

    List<CarAccessoryResponse> getAll();
}
