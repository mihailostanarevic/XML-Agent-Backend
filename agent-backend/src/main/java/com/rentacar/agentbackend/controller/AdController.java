package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.AddAdRequest;
import com.rentacar.agentbackend.dto.request.RequestDTO;
import com.rentacar.agentbackend.dto.request.UpdateAdRequest;
import com.rentacar.agentbackend.dto.response.AdResponse;
import com.rentacar.agentbackend.dto.response.PhotoResponse;
import com.rentacar.agentbackend.dto.response.RequestResponse;
import com.rentacar.agentbackend.entity.Photo;
import com.rentacar.agentbackend.repository.IPhotoRepository;
import com.rentacar.agentbackend.service.IAdService;
import com.rentacar.agentbackend.service.IRequestService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/ads")
public class AdController {

    private final IAdService _adService;
    private final IRequestService _requestService;

    public AdController(IAdService adService, IRequestService requestService) {
        _adService = adService;
        _requestService = requestService;
    }

    @PostMapping("/image")
    @PreAuthorize("hasAuthority('VIEW_IMAGE')")
    public ResponseEntity<?> image(@RequestParam("imageFile") List<MultipartFile> file) throws Exception{
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @PreAuthorize("hasAuthority('CREATE_AD')")
    public ResponseEntity<?> addAd(@RequestPart("imageFile") List<MultipartFile> fileList, @RequestPart("request") @Valid AddAdRequest request) throws Exception{
        return new ResponseEntity<>(_adService.createAd(fileList, request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/image" )
    @PreAuthorize("hasAuthority('VIEW_IMAGE')")
    public ResponseEntity<PhotoResponse> getImage(@PathVariable("id") UUID adId) {
        return new ResponseEntity<>(_adService.getPhoto(adId), HttpStatus.OK);
    }

    @PostMapping("/availability")
    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    public ResponseEntity<?> changeCarAvailability(@RequestBody RequestDTO request) throws Exception{
        _requestService.changeAdAvailability(request);
        return new ResponseEntity<>(new RequestResponse("succesfully changed"), HttpStatus.OK);
    }

    @PutMapping("/{id}/ad")
    public AdResponse updateAd(@RequestBody UpdateAdRequest request, @PathVariable UUID id) throws Exception{
        return _adService.updateAd(request, id);
    }

    @DeleteMapping("/{id}/ad")
    public void deleteAd(@PathVariable UUID id) throws Exception{
        _adService.deleteAd(id);
    }

    @GetMapping("/{id}/ad")
    public AdResponse getAd(@PathVariable UUID id) throws Exception{
        return _adService.getAd(id);
    }

    @GetMapping
    public List<AdResponse> getAllAds() throws Exception{
        return _adService.getAllAds();
    }

    @GetMapping("/{id}/car-brand")
    public List<AdResponse> getAdByCarBrand(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByCarBrand(id);
    }

    @GetMapping("/{id}/car-model")
    public List<AdResponse> getAdByCarModel(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByCarModel(id);
    }

    @GetMapping("/{id}/car-class")
    public List<AdResponse> getAdByCarClass(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByCarClass(id);
    }

    @GetMapping("/{id}/gearshift-type")
    public List<AdResponse> getAdByGearshiftType(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByGearshiftType(id);
    }

    @GetMapping("/{id}/fuel-type")
    public List<AdResponse> getAdByFuelType(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByFuelType(id);
    }

    @GetMapping("/with-gas")
    public List<AdResponse> getAllByGas() throws Exception{
        return _adService.getAllAdsByGas();
    }
}

