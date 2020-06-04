package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.AddCarAccessoriesRequest;
import com.rentacar.agentbackend.dto.request.AddKilometersRequest;
import com.rentacar.agentbackend.dto.request.CreateCarRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarRequest;
import com.rentacar.agentbackend.dto.response.CarResponse;
import com.rentacar.agentbackend.entity.Car;
import com.rentacar.agentbackend.entity.CarAccessories;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.ICarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarService implements ICarService {

    private final ICarRepository _carRepository;

    private final ICarModelRepository _carModelRepository;

    private final IGearshiftTypeRepository _gearshiftTypeRepository;

    private final IFuelTypeRepository _fuelTypeRepository;

    private final ICarAccessoriesRepository _carAccessoriesRepository;

    public CarService(ICarRepository carRepository, ICarModelRepository carModelRepository, IGearshiftTypeRepository gearshiftTypeRepository, IFuelTypeRepository fuelTypeRepository, ICarAccessoriesRepository carAccessoriesRepository) {
        _carRepository = carRepository;
        _carModelRepository = carModelRepository;
        _gearshiftTypeRepository = gearshiftTypeRepository;
        _fuelTypeRepository = fuelTypeRepository;
        _carAccessoriesRepository = carAccessoriesRepository;
    }

    @Override
    public CarResponse createCar(CreateCarRequest request) throws Exception {
        Car car = new Car();
        car.setDeleted(false);
        car.setKilometersTraveled(request.getKilometersTraveled());
        car.setCarModel(_carModelRepository.findOneById(request.getCarModelId()));
        car.setGearshiftType(_gearshiftTypeRepository.findOneById(request.getGearshiftTypeId()));
        car.setFuelType(_fuelTypeRepository.findOneById(request.getFuelTypeId()));
        Car savedCar = _carRepository.save(car);
        return mapCarToCarResponse(savedCar);
    }

    @Override
    public CarResponse updateCar(UpdateCarRequest request, UUID id) throws Exception {
        Car car = _carRepository.findOneById(id);
        car.setKilometersTraveled(request.getKilometersTraveled());
        car.getFuelType().setGas(request.isGas());
        _fuelTypeRepository.save(car.getFuelType());
        Car savedCar = _carRepository.save(car);
        return mapCarToCarResponse(savedCar);
    }

    @Override
    public void deleteCar(UUID id) throws Exception {
        Car car = _carRepository.findOneById(id);
        car.setDeleted(true);
        _carRepository.save(car);
    }

    @Override
    public CarResponse getCar(UUID id) throws Exception {
        Car car = _carRepository.findOneById(id);
        return mapCarToCarResponse(car);
    }

    @Override
    public List<CarResponse> getAllCars() throws Exception {
        List<Car> cars = _carRepository.findAllByDeleted(false);
        return cars.stream()
                .map(car -> mapCarToCarResponse(car))
                .collect(Collectors.toList());
    }

    @Override
    public void addKilometers(AddKilometersRequest request, UUID id) throws Exception {
        Car car = _carRepository.findOneById(id);
        Float kilometers = Float.valueOf(car.getKilometersTraveled());
        kilometers += Float.valueOf(request.getKilometersTraveled());
        car.setKilometersTraveled(kilometers.toString());
        _carRepository.save(car);
    }

    @Override
    public void addCarAccessories(AddCarAccessoriesRequest request) throws Exception {
        Car car = _carRepository.findOneById(request.getCarId());
        CarAccessories carAccessories = _carAccessoriesRepository.findOneById(request.getCarAccessoriesId());
        car.getCarAccessories().add(carAccessories);
        _carRepository.save(car);
        carAccessories.getCars().add(car);
        _carAccessoriesRepository.save(carAccessories);
    }

    private CarResponse mapCarToCarResponse(Car car) {
        CarResponse response = new CarResponse();
        response.setId(car.getId());
        response.setKilometersTraveled(car.getKilometersTraveled());
        response.setCarBrandName(car.getCarModel().getCarBrand().getName());
        response.setCarBrandCountry(car.getCarModel().getCarBrand().getCountry());
        response.setCarClassName(car.getCarModel().getCarClass().getName());
        response.setCarClassDescription(car.getCarModel().getCarClass().getDescription());
        response.setCarModelName(car.getCarModel().getName());
        response.setGearshiftTypeType(car.getGearshiftType().getType());
        response.setGearshiftTypeNumberOfGears(car.getGearshiftType().getNumberOfGears());
        response.setFuelTypeType(car.getFuelType().getType());
        response.setFuelTypeTankCapacity(car.getFuelType().getTankCapacity());
        response.setFuelTypeGas(car.getFuelType().isGas());
        return response;
    }
}
