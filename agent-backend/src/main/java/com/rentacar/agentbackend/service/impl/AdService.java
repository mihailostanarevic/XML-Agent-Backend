package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.AddAdRequest;
import com.rentacar.agentbackend.dto.request.UpdateAdRequest;
import com.rentacar.agentbackend.dto.response.AdResponse;
import com.rentacar.agentbackend.entity.Ad;
import com.rentacar.agentbackend.entity.Car;
import com.rentacar.agentbackend.entity.Photo;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IAdService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unused", "RedundantThrows"})
@Service
public class AdService implements IAdService {

    private final IAdRepository _adRepository;
    private final ICarModelRepository _carModelRepository;
    private final IGearshiftTypeRepository _gearshiftTypeRepository;
    private final IFuelTypeRepository _fuelTypeRepository;
    private final ICarRepository _carRepository;
    private final IAgentRepository _agentRepository;
    private final IPhotoRepository _photoRepository;
    private final IRequestAdRepository _requestAdRepository;

    public AdService(IAdRepository adRepository, ICarModelRepository carModelRepository, IGearshiftTypeRepository gearshiftTypeRepository, IFuelTypeRepository fuelTypeRepository, ICarRepository carRepository, IAgentRepository agentRepository, IPhotoRepository photoRepository, IRequestAdRepository requestAdRepository) {
        _adRepository = adRepository;
        _carModelRepository = carModelRepository;
        _gearshiftTypeRepository = gearshiftTypeRepository;
        _fuelTypeRepository = fuelTypeRepository;
        _carRepository = carRepository;
        _agentRepository = agentRepository;
        _photoRepository = photoRepository;
        _requestAdRepository = requestAdRepository;
    }

    @Override
    public AdResponse createAd(AddAdRequest request) throws GeneralException {
        Car car = new Car();
        car.setCarModel(_carModelRepository.findOneById(request.getCarModelId()));
        car.setGearshiftType(_gearshiftTypeRepository.findOneById(request.getGearshifTypeId()));
        car.setFuelType(_fuelTypeRepository.findOneById(request.getFuelTypeId()));
        car.setKilometersTraveled(request.getKilometersTraveled());
        Car savedCar = _carRepository.save(car);
        Ad ad = new Ad();
        ad.setAgent(_agentRepository.findOneById(request.getAgentId()));
        ad.setCar(savedCar);
        ad.setLimitedDistance(request.isLimitedDistance());
        ad.setAvailableKilometersPerRent(request.getAvailableKilometersPerRent());
        ad.setSeats(request.getSeats());
        ad.setCdw(request.isCdw());
        _adRepository.save(ad);
        for (String photoUrl : request.getPhotoUrls()) {
            Photo photo = new Photo();
            photo.setUrl(photoUrl);
            photo.setAd(ad);
            _photoRepository.save(photo);
        }

        return mapAdToAdResponse(ad);
    }

    private AdResponse mapAdToAdResponse(Ad ad) {
        AdResponse adResponse = new AdResponse();
        adResponse.setId(ad.getId());
        return adResponse;
    }

    @Override
    public AdResponse updateAd(UpdateAdRequest request, UUID id) throws Exception {
        return null;
    }

    @Override
    public void deleteAd(UUID id) throws Exception {

    }

    @Override
    public AdResponse getAd(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAds() throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAdsByCarModel(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAdsByCarBrand(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAdsByCarClass(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAdsByGearshiftType(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAdsByFuelType(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<AdResponse> getAllAdsByGas() throws Exception {
        return null;
    }

}
