package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreateAddressRequest;

public interface IAddressService {

    void createAddress(CreateAddressRequest request) throws Exception;
}
