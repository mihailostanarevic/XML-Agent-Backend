package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateAddressRequest;
import com.rentacar.agentbackend.service.IAddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final IAddressService _addressService;

    public AddressController(IAddressService addressService) {
        _addressService = addressService;
    }

    @PostMapping
    public void createAddress(@RequestBody CreateAddressRequest request) throws Exception {
        _addressService.createAddress(request);
    }
}
