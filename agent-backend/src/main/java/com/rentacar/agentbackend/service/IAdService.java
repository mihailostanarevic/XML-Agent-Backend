package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.AddAdRequest;
import com.rentacar.agentbackend.dto.request.UpdateAdRequest;
import com.rentacar.agentbackend.dto.response.AdResponse;
import com.rentacar.agentbackend.dto.response.PhotoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IAdService {

    AdResponse createAd(List<MultipartFile> fileList, AddAdRequest request) throws Exception;

    AdResponse updateAd(UpdateAdRequest request, UUID id) throws Exception;

    void deleteAd(UUID id) throws Exception;

    AdResponse getAd(UUID id) throws Exception;

    List<AdResponse> getAllAds() throws Exception;

    List<AdResponse> getAllAdsByCarModel(UUID id) throws Exception;

    List<AdResponse> getAllAdsByCarBrand(UUID id) throws Exception;

    List<AdResponse> getAllAdsByCarClass(UUID id) throws Exception;

    List<AdResponse> getAllAdsByGearshiftType(UUID id) throws Exception;

    List<AdResponse> getAllAdsByFuelType(UUID id) throws Exception;

    List<AdResponse> getAllAdsByGas() throws Exception;

    PhotoResponse getPhoto(UUID adId);

    List<AdResponse> getAgentAds(UUID id);

    List<PhotoResponse> getAllPhotos(UUID adID);
}
