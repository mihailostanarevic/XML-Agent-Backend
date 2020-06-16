package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.*;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.IRequestAdRepository;
import com.rentacar.agentbackend.repository.IRequestRepository;
import com.rentacar.agentbackend.repository.IUserRepository;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IRequestAdRepository _requestAdRepository;

    private final IRequestRepository _requestRepository;

    private final IUserRepository _userRepository;

    public UserService(IRequestAdRepository requestAdRepository, IRequestRepository requestRepository, IUserRepository userRepository) {
        _requestAdRepository = requestAdRepository;
        _requestRepository = requestRepository;
        _userRepository = userRepository;
    }

    public User findOneByUsername(String mail) {
        return _userRepository.findOneByUsername(mail);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = _userRepository.findAllByDeleted(false);
        return users.stream()
                .map(user -> mapUserToUserResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(UUID id) {
        return _userRepository.findOneById(id);
    }

    @Override
    public List<UsersAdsResponse> getUsersRequestFromStatus(UUID id, RequestStatus status) {
        List<UsersAdsResponse> retVal = new ArrayList<>();
        List<Request> requests = _requestRepository.findAllByStatus(status);

        for(Request request : requests){
            if(request.getCustomer().getId().equals(id)){
                for(RequestAd rqAd : request.getRequestAds()){
                    retVal.add(makeUsersAdDTO(rqAd));
                }
            }
        }

        return retVal;
    }

    private UsersAdsResponse makeUsersAdDTO(RequestAd requestAd){
        Ad ad = requestAd.getAd();
        UsersAdsResponse retVal = new UsersAdsResponse();

        List<PhotoSearchResponse> photos = new ArrayList<PhotoSearchResponse>();
        AdSearchResponse adDTO = new AdSearchResponse(ad.getId(), ad.isLimitedDistance(), ad.getSeats(), ad.isCdw(), ad.getCreationDate(), photos);
        CarSearchResponse carDTO = new CarSearchResponse();
        carDTO.setCarID(ad.getCar().getId());
        carDTO.setCarModelName(ad.getCar().getCarModel().getName());
        carDTO.setCarBrandName(ad.getCar().getCarModel().getCarBrand().getName());
        carDTO.setCarClassName(ad.getCar().getCarModel().getCarClass().getName());
        carDTO.setCarClassDesc(ad.getCar().getCarModel().getCarClass().getDescription());
        carDTO.setFuelTypeType(ad.getCar().getFuelType().getType());
        carDTO.setFuelTypeTankCapacity(ad.getCar().getFuelType().getTankCapacity());
        carDTO.setFuelTypeGas(ad.getCar().getFuelType().isGas());
        carDTO.setGearshiftTypeType(ad.getCar().getGearshiftType().getType());
        carDTO.setGetGearshiftTypeNumberOfGears(ad.getCar().getGearshiftType().getNumberOfGears());
        String allLocations = "";
        for(Address add : ad.getAgent().getAddress()){
            allLocations += add.getCity();
            if(ad.getAgent().getAddress().size() > 1){
                allLocations += ", ";
            }
        }
        AgentSearchResponse agentDTO = new AgentSearchResponse(ad.getAgent().getId(), ad.getAgent().getName(), ad.getAgent().getDateFounded().toString(), allLocations);
        retVal.setAd(adDTO);
        retVal.setAgent(agentDTO);
        retVal.setCar(carDTO);
        retVal.setDateFrom(requestAd.getPickUpDate().toString());
        retVal.setDateTo(requestAd.getReturnDate().toString());
        retVal.setTimeFrom(requestAd.getPickUpTime().toString());
        retVal.setTimeTo(requestAd.getReturnTime().toString());

        return retVal;
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }
}
