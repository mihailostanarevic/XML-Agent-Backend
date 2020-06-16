package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.response.CarAccessoryResponse;
import com.rentacar.agentbackend.service.ICarAccessoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/car-accessories")
public class CarAccessoriesController {

    @Autowired
    private ICarAccessoriesService _carAccessoriesService;

    @GetMapping
    public List<CarAccessoryResponse> getAll(){
        return _carAccessoriesService.getAll();
    }
}
