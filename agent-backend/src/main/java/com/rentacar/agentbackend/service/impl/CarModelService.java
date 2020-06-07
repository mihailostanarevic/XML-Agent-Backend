package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateCarModelRequest;
import com.rentacar.agentbackend.dto.request.GetCarModelsFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarModelRequest;
import com.rentacar.agentbackend.dto.response.CarModelResponse;
import com.rentacar.agentbackend.entity.CarModel;
import com.rentacar.agentbackend.repository.ICarBrandRepository;
import com.rentacar.agentbackend.repository.ICarClassRepository;
import com.rentacar.agentbackend.repository.ICarModelRepository;
import com.rentacar.agentbackend.service.ICarModelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarModelService implements ICarModelService {

    private final ICarModelRepository _carModelRepository;

    private final ICarBrandRepository _carBrandRepository;

    private final ICarClassRepository _carClassRepository;

    public CarModelService(ICarModelRepository carModelRepository, ICarBrandRepository carBrandRepository, ICarClassRepository carClassRepository) {
        _carModelRepository = carModelRepository;
        _carBrandRepository = carBrandRepository;
        _carClassRepository = carClassRepository;
    }

    @Override
    public CarModelResponse createCarModel(CreateCarModelRequest request) throws Exception {
        CarModel carModel = new CarModel();
        carModel.setDeleted(false);
        carModel.setName(request.getName());
        carModel.setCarBrand(_carBrandRepository.findOneById(request.getBrandId()));
        carModel.setCarClass(_carClassRepository.findOneById(request.getClassId()));
        CarModel savedCarModel = _carModelRepository.save(carModel);
        return mapCarModelToCarModelResponse(savedCarModel);
    }

    @Override
    public CarModelResponse updateCarModel(UpdateCarModelRequest request, UUID id) throws Exception {
        CarModel carModel = _carModelRepository.findOneById(id);
        carModel.setName(request.getName());
        CarModel savedCarModel = _carModelRepository.save(carModel);
        return mapCarModelToCarModelResponse(savedCarModel);
    }

    @Override
    public void deleteCarModel(UUID id) throws Exception {
        CarModel carModel = _carModelRepository.findOneById(id);
        carModel.setDeleted(true);
        _carModelRepository.save(carModel);
    }

    @Override
    public CarModelResponse getCarModel(UUID id) throws Exception {
        CarModel carModel = _carModelRepository.findOneById(id);
        return mapCarModelToCarModelResponse(carModel);
    }

    @Override
    public List<CarModelResponse> getAllCarModels() throws Exception {
        List<CarModel> carModels = _carModelRepository.findAllByDeleted(false);
        return carModels.stream()
                .map(carModel -> mapCarModelToCarModelResponse(carModel))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarModelResponse> getAllCarModelsWithFilter(GetCarModelsFilterRequest request) throws Exception {
        List<CarModel> allCarModels = _carModelRepository.findAllByDeleted(false);

        return allCarModels
                .stream()
                .filter(carModel -> {
                    if(request.getBrandName() != null) {
                        return carModel.getCarBrand().getName().toLowerCase().contains(request.getBrandName().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .filter(carModel -> {
                    if(request.getClassName() != null) {
                        return carModel.getCarClass().getName().toLowerCase().contains(request.getClassName().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .map(cm -> mapCarModelToCarModelResponse(cm))
                .collect(Collectors.toList());
    }

    private CarModelResponse mapCarModelToCarModelResponse(CarModel carModel) {
        CarModelResponse response = new CarModelResponse();
        response.setId(carModel.getId());
        response.setName(carModel.getName());
        response.setBrandName(carModel.getCarBrand().getName());
        response.setClassName(carModel.getCarClass().getName());
        return response;
    }
}
