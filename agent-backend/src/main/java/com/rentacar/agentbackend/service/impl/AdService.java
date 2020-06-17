package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.AddAdRequest;
import com.rentacar.agentbackend.dto.request.UpdateAdRequest;
import com.rentacar.agentbackend.dto.response.AdResponse;
import com.rentacar.agentbackend.dto.response.PhotoResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IAdService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
    public PhotoResponse getPhoto(UUID adId) {
        Ad ad = _adRepository.findOneById(adId);
        Photo photo = ad.getAdPhotos().iterator().next();   // bilo koja slika
        Photo img = new Photo(photo.getName(), photo.getType(), decompressBytes(photo.getPicByte()), false, ad);
        return mapToPhotoResponse(img);
    }

    private PhotoResponse mapToPhotoResponse(Photo img) {
        PhotoResponse photoResponse = new PhotoResponse();
        photoResponse.setName(img.getName());
        photoResponse.setType(img.getType());
        photoResponse.setPicByte(img.getPicByte());
        return photoResponse;
    }

    @Override
    public AdResponse createAd(List<MultipartFile> fileList, AddAdRequest request) throws GeneralException, IOException {
        CarModel carModel = findCarModel(request.getCarModel());
        GearshiftType gearshiftType = findGearshiftType(request.getGearshifType());
        FuelType fuelType = findFuelType(request.getFuelType());
        Car car = new Car();
        car.setCarModel(carModel);
        car.setGearshiftType(gearshiftType);
        car.setFuelType(fuelType);
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
        for (MultipartFile file : fileList) {
            Photo photo = new Photo();
            photo.setAd(ad);
            photo.setName(file.getOriginalFilename());
            photo.setType(file.getContentType());
            photo.setPicByte(compressBytes(file.getBytes()));
            _photoRepository.save(photo);
        }

        return mapAdToAdResponse(ad);
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }
    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    private CarModel findCarModel(String carModelString) {
        String[] carModelArray = carModelString.split(",");
        String carBrand = carModelArray[0].trim();
        String carModelName = carModelArray[1].trim();
        String carClass = carModelArray[2].trim();
        for (CarModel carModel : _carModelRepository.findAll()) {
            if(carModel.getCarBrand().getName().equalsIgnoreCase(carBrand)
                && carModel.getName().equalsIgnoreCase(carModelName)
                    && carModel.getCarClass().getName().equalsIgnoreCase(carClass)){
                return carModel;
            }
        }
        return null;
    }

    private GearshiftType findGearshiftType(String gearshifTypeString) {
        String[] gearshiftTypeArray = gearshifTypeString.split(",");
        String type = gearshiftTypeArray[0].trim();
        String numberOfGears = gearshiftTypeArray[1].trim();
        for (GearshiftType gearshiftType : _gearshiftTypeRepository.findAll()) {
            if(gearshiftType.getType().equalsIgnoreCase(type)
                && gearshiftType.getNumberOfGears().equalsIgnoreCase(numberOfGears)) {
                    return gearshiftType;
            }
        }
        return null;
    }

    private FuelType findFuelType(String fuelTypeString) {
        String[] fuelTypeArray = fuelTypeString.split(",");
        String type = fuelTypeArray[0].trim();
        String tankCapacity = fuelTypeArray[1].trim();
        String gas_string = fuelTypeArray[2].trim();
        boolean gas = false;
        if(gas_string.trim().equalsIgnoreCase("gas")) {
            gas = true;
        }
        for (FuelType fuelType : _fuelTypeRepository.findAll()) {
            if(fuelType.getType().equalsIgnoreCase(type)
                && fuelType.getTankCapacity().equalsIgnoreCase(tankCapacity)
                    && fuelType.isGas() == gas) {
                    return fuelType;
            }
        }
        return null;
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
