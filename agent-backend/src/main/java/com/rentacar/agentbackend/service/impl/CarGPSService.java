package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.TrackCarRequest;
import com.rentacar.agentbackend.dto.response.CarGpsResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.IAdRepository;
import com.rentacar.agentbackend.repository.ICarGPSRepository;
import com.rentacar.agentbackend.repository.ICarRepository;
import com.rentacar.agentbackend.repository.ISimpleUserRepository;
import com.rentacar.agentbackend.service.ICarGSPService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarGPSService implements ICarGSPService {

    private final ICarGPSRepository _carGPSRepository;

    private final ICarRepository _carRepository;

    private final ISimpleUserRepository _simpleUserRepository;

    private final IAdRepository _adRepository;

    public CarGPSService(ICarGPSRepository carGPSRepository, ICarRepository carRepository, ISimpleUserRepository simpleUserRepository, IAdRepository adRepository) {
        _carGPSRepository = carGPSRepository;
        _carRepository = carRepository;
        _simpleUserRepository = simpleUserRepository;
        _adRepository = adRepository;
    }

    @Override
    public CarGpsResponse trackCar(TrackCarRequest request) throws Exception {
        CarGPS carGPS = _carGPSRepository.findOneByCarId(request.getCarId());
        if(carGPS == null){
            CarGPS newCarGPS = new CarGPS();
            Ad ad = _adRepository.findOneByDeletedAndCar_Id(false, request.getCarId());
            for(RequestAd ra: ad.getAdRequests()){
                LocalDate now = LocalDate.now();
                if(ra.getPickUpDate().isBefore(now) && ra.getReturnDate().isAfter(now)){
                    newCarGPS.setCustomerId(ra.getRequest().getCustomer().getId());
                    newCarGPS.setCarId(request.getCarId());
                    newCarGPS.setDeleted(false);
                    double lat = 43.89992 + new Random().nextDouble() * (45.45343 - 43.89992);
                    newCarGPS.setLat(String.valueOf(lat));
                    double lng = 22.99932 + new Random().nextDouble() * (24.89423 - 22.99932);
                    newCarGPS.setLng(String.valueOf(lng));
                    CarGPS savedCarGPS = _carGPSRepository.save(newCarGPS);
                    return mapCarGPSToCarGPSResponse(savedCarGPS);
                }
            }
        }
        Ad ad = _adRepository.findOneByDeletedAndCar_Id(false, request.getCarId());
        for(RequestAd ra: ad.getAdRequests()){
            LocalDate now = LocalDate.now();
            if(ra.getPickUpDate().isBefore(now) && ra.getReturnDate().isAfter(now)){
                carGPS.setCustomerId(ra.getRequest().getCustomer().getId());
                carGPS.setCarId(request.getCarId());
                carGPS.setDeleted(false);
                double lat = 42.55139 + new Random().nextDouble() * (46.102792 - 42.55139);
                carGPS.setLat(String.valueOf(lat));
                double lng = 18.98472 + new Random().nextDouble() * (22.58611 - 18.98472);
                carGPS.setLng(String.valueOf(lng));
                CarGPS savedCarGPS = _carGPSRepository.save(carGPS);
                return mapCarGPSToCarGPSResponse(savedCarGPS);
            }
        }
        return null;
    }

    @Override
    public List<CarGpsResponse> getAllCarsWhichCanBeTrackedByAgent(UUID id) throws Exception {
        List<Ad> ads = _adRepository.findAllByDeleted(false);
        List<Ad> ads1 = new ArrayList<>();
        SimpleUser simpleUser = null;
        for(Ad ad: ads){
            if(ad.getAgent().getId().equals(id)){
                for(RequestAd ra: ad.getAdRequests()){
                    LocalDate now = LocalDate.now();
                    if(ra.getPickUpDate().isBefore(now) && ra.getReturnDate().isAfter(now)){
                        ads1.add(ad);
                        simpleUser = ra.getRequest().getCustomer();
                        break;
                    }
                }
            }
        }
        List<CarGpsResponse> responses = new ArrayList<>();
        for(Ad ad: ads1){
            CarGpsResponse response = new CarGpsResponse();
            response.setCarId(ad.getCar().getId());
            response.setBrandName(ad.getCar().getCarModel().getCarBrand().getName());
            response.setModelName(ad.getCar().getCarModel().getName());
            response.setCustomer(simpleUser.getFirstName() + ' ' + simpleUser.getLastName());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<CarGpsResponse> getAllTrackedCarsByAgent(UUID id) throws Exception {
        List<CarGPS> list = _carGPSRepository.findAllByDeleted(false);
        List<CarGPS> listByAgent = new ArrayList<>();
        for (CarGPS cg: list){
            Car car = _carRepository.findOneById(cg.getCarId());
            if(car.getAd().getAgent().getId().equals(id)){
                boolean flag = false;
                LocalDate now = LocalDate.now();
                for(RequestAd ra: car.getAd().getAdRequests()){
                    if(ra.getPickUpDate().isBefore(now) && ra.getReturnDate().isAfter(now)){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    listByAgent.add(cg);
                }

            }
        }

        return listByAgent.stream()
                .map(carGPS -> mapCarGPSToCarGPSResponse(carGPS))
                .collect(Collectors.toList());
    }

    @Override
    public void tracking() throws Exception {
        List<CarGPS> list = _carGPSRepository.findAllByDeleted(false);
        for(CarGPS cg: list){
            Random rnd =  new Random();
            int switcher = 1 + rnd.nextInt(4);
            double newLat;
            double newLng;
            switch (switcher){
                case 1:
                    newLat = Double.valueOf(cg.getLat()) + 0.2;
                    newLng = Double.valueOf(cg.getLng()) + 0.2;
                    cg.setLat(String.valueOf(newLat));
                    cg.setLng(String.valueOf(newLng));
                    _carGPSRepository.save(cg);
                    break;
                case 2:
                    newLat = Double.valueOf(cg.getLat()) + 0.2;
                    newLng = Double.valueOf(cg.getLng()) - 0.2;
                    cg.setLat(String.valueOf(newLat));
                    cg.setLng(String.valueOf(newLng));
                    _carGPSRepository.save(cg);
                    break;
                case 3:
                    newLat = Double.valueOf(cg.getLat()) - 0.2;
                    newLng = Double.valueOf(cg.getLng()) + 0.2;
                    cg.setLat(String.valueOf(newLat));
                    cg.setLng(String.valueOf(newLng));
                    _carGPSRepository.save(cg);
                    break;
                default: //case 4:
                    newLat = Double.valueOf(cg.getLat()) - 0.2;
                    newLng = Double.valueOf(cg.getLng()) - 0.2;
                    cg.setLat(String.valueOf(newLat));
                    cg.setLng(String.valueOf(newLng));
                    _carGPSRepository.save(cg);
                    break;
            }
            System.out.println("carId: " + cg.getCarId() + " lat: " + cg.getLat() + " lng: " + cg.getLng());
        }
    }

    private CarGpsResponse mapCarGPSToCarGPSResponse(CarGPS gps) {
        CarGpsResponse carGpsResponse = new CarGpsResponse();
        carGpsResponse.setCarId(gps.getCarId());
        carGpsResponse.setId(gps.getId());
        carGpsResponse.setLat(gps.getLat());
        carGpsResponse.setLng(gps.getLng());
        Car car = _carRepository.findOneById(gps.getCarId());
        carGpsResponse.setBrandName(car.getCarModel().getCarBrand().getName());
        carGpsResponse.setModelName(car.getCarModel().getName());
        SimpleUser simpleUser = _simpleUserRepository.findOneById(gps.getCustomerId());
        carGpsResponse.setCustomer(simpleUser.getFirstName() + ' ' + simpleUser.getLastName());
        carGpsResponse.setCustomerId(simpleUser.getId());
        return carGpsResponse;
    }
}
