package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateCarBrandRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarBrandRequest;
import com.rentacar.agentbackend.dto.response.CarBrandResponse;
import com.rentacar.agentbackend.entity.CarBrand;
import com.rentacar.agentbackend.repository.ICarBrandRepository;
import com.rentacar.agentbackend.service.ICarBrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarBrandService implements ICarBrandService {

    private final ICarBrandRepository _carBrandRepository;

    public CarBrandService(ICarBrandRepository carBrandRepository) {
        _carBrandRepository = carBrandRepository;
    }

    @Override
    public CarBrandResponse createCarBrand(CreateCarBrandRequest request) throws Exception {
        CarBrand carBrand = new CarBrand();
        carBrand.setDeleted(false);
        carBrand.setName(request.getName());
        carBrand.setCountry(request.getCountry());
        CarBrand savedCarBrand = _carBrandRepository.save(carBrand);
        return mapCarBrandToCarBrandResponse(savedCarBrand);
    }

    @Override
    public CarBrandResponse updateCarBrand(UpdateCarBrandRequest request, UUID id) throws Exception {
        CarBrand carBrand = _carBrandRepository.findOneById(id);
        carBrand.setName(request.getName());
        carBrand.setCountry(request.getCountry());
        CarBrand savedCarBrand = _carBrandRepository.save(carBrand);
        return mapCarBrandToCarBrandResponse(savedCarBrand);
    }

    @Override
    public void deleteCarBrand(UUID id) throws Exception {
        CarBrand carBrand = _carBrandRepository.findOneById(id);
        carBrand.setDeleted(true);
        _carBrandRepository.save(carBrand);
    }

    @Override
    public CarBrandResponse getCarBrand(UUID id) throws Exception {
        CarBrand carBrand = _carBrandRepository.findOneById(id);
        return mapCarBrandToCarBrandResponse(carBrand);
    }

    @Override
    public List<CarBrandResponse> getAllCarBrands() throws Exception {
        List<CarBrand> carBrands = _carBrandRepository.findAllByDeleted(false);
        return  carBrands.stream()
                .map(carBrand -> mapCarBrandToCarBrandResponse(carBrand))
                .collect(Collectors.toList());
    }

    private CarBrandResponse mapCarBrandToCarBrandResponse(CarBrand carBrand) {
        CarBrandResponse response = new CarBrandResponse();
        response.setId(carBrand.getId());
        response.setName(carBrand.getName());
        response.setCountry(carBrand.getCountry());
        return response;
    }
}
