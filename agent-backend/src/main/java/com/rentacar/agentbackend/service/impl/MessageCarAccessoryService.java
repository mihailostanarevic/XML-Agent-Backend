package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.ApproveDenyAccessoryRequest;
import com.rentacar.agentbackend.entity.Car;
import com.rentacar.agentbackend.entity.MessageCarAccessories;
import com.rentacar.agentbackend.repository.ICarAccessoriesRepository;
import com.rentacar.agentbackend.repository.ICarRepository;
import com.rentacar.agentbackend.repository.IMessageCarAccessoriesRepository;
import com.rentacar.agentbackend.service.IMessageCarAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageCarAccessoryService implements IMessageCarAccessoryService {

    @Autowired
    private IMessageCarAccessoriesRepository _messageCarAccessoriesRepository;

    @Autowired
    private ICarAccessoriesRepository _carAccessoriesRepository;

    @Autowired
    private ICarRepository _carRepository;

    @Override
    public void approveDenyAccessory(ApproveDenyAccessoryRequest request) {
        MessageCarAccessories tuple = _messageCarAccessoriesRepository.getOne(request.getId());
        tuple.setApproved(request.isApproved());

        Car car = _carRepository.getOne(tuple.getMessage().getAd().getCar().getId());
        car.getCarAccessories().add(_carAccessoriesRepository.getOne(tuple.getCar_accessory().getId()));
        tuple.setReviewed(true);
        _messageCarAccessoriesRepository.save(tuple);
    }
}
