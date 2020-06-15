package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.CarAccessoryResponse;
import com.rentacar.agentbackend.entity.CarAccessories;
import com.rentacar.agentbackend.repository.ICarAccessoriesRepository;
import com.rentacar.agentbackend.service.ICarAccessoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarAccessoriesService implements ICarAccessoriesService {

    @Autowired
    private ICarAccessoriesRepository _carAccessoriesRepository;

    @Override
    public List<CarAccessoryResponse> getAll() {
        return mapCarAccessoriesToResponse(_carAccessoriesRepository.findAll()) ;
    }

    public List<CarAccessoryResponse> mapCarAccessoriesToResponse(List<CarAccessories> carAccessories) {
        List<CarAccessoryResponse> retVal = new ArrayList<>();

        for(CarAccessories carAccessory : carAccessories){
            if(!carAccessory.isDeleted()){
                CarAccessoryResponse dto = new CarAccessoryResponse(carAccessory.getId(), carAccessory.getDescription());
                retVal.add(dto);
            }
        }

        return retVal;
    }
}
