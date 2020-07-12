package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateCarClassRequest;
import com.rentacar.agentbackend.dto.request.GetCarClassesWithFilter;
import com.rentacar.agentbackend.dto.request.UpdateCarClassRequest;
import com.rentacar.agentbackend.dto.response.CarClassResponse;
import com.rentacar.agentbackend.entity.CarClass;
import com.rentacar.agentbackend.repository.ICarClassRepository;
import com.rentacar.agentbackend.service.ICarClassService;
import com.rentacar.agentbackend.soap.CarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarClassService implements ICarClassService {

    private final ICarClassRepository _carClassRepository;

    @Autowired
    private CarClient carClient;

    public CarClassService(ICarClassRepository carClassRepository) {
        _carClassRepository = carClassRepository;
    }

    @Override
    public CarClassResponse createCarClass(CreateCarClassRequest request) throws Exception {
        CarClass carClass = new CarClass();
        carClass.setDeleted(false);
        carClass.setName(request.getName());
        carClass.setDescription(request.getDescription());
        CarClass savedCarClass = _carClassRepository.save(carClass);
        com.rentacar.agentbackend.soap.wsdl.CarClass carClassSOAP = new com.rentacar.agentbackend.soap.wsdl.CarClass();
        carClassSOAP.setCarClassID(savedCarClass.getId().toString());
        carClassSOAP.setName(request.getName());
        carClassSOAP.setDescription(request.getDescription());
        carClassSOAP.setDeleted(savedCarClass.isDeleted());
        carClient.createCarClass(carClassSOAP);
        return mapCarClassToCarClassResponse(savedCarClass);
    }

    @Override
    public CarClassResponse updateCarClass(UpdateCarClassRequest request, UUID id) throws Exception {
        CarClass carClass = _carClassRepository.findOneById(id);
        carClass.setName(request.getName());
        carClass.setDescription(request.getDescription());
        CarClass savedCarClass = _carClassRepository.save(carClass);
        return mapCarClassToCarClassResponse(savedCarClass);
    }

    @Override
    public void deleteCarClass(UUID id) throws Exception {
        CarClass carClass = _carClassRepository.findOneById(id);
        carClass.setDeleted(true);
        _carClassRepository.save(carClass);
    }

    @Override
    public CarClassResponse getCarClass(UUID id) throws Exception {
        CarClass carClass = _carClassRepository.findOneById(id);
        return mapCarClassToCarClassResponse(carClass);
    }

    @Override
    public List<CarClassResponse> getAllCarClasses() throws Exception {
        List<CarClass> carClasses = _carClassRepository.findAllByDeleted(false);
        return carClasses.stream()
                .map(carClass -> mapCarClassToCarClassResponse(carClass))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarClassResponse> getAllCarClassesWithFilter(GetCarClassesWithFilter request) throws Exception {
        List<CarClass> allCarClasses = _carClassRepository.findAllByDeleted(false);
        return allCarClasses
                .stream()
                .filter(carClass -> {
                    if(request.getClassName() != null) {
                        return carClass.getName().toLowerCase().contains(request.getClassName().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .map(cc -> mapCarClassToCarClassResponse(cc))
                .collect(Collectors.toList());
    }

    private CarClassResponse mapCarClassToCarClassResponse(CarClass carClass) {
        CarClassResponse response = new CarClassResponse();
        response.setId(carClass.getId());
        response.setDescription(carClass.getDescription());
        response.setName(carClass.getName());
        return response;
    }
}
