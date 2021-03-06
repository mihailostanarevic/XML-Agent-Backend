package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.*;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IAgentService;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IRequestAdRepository _requestAdRepository;
    private final IRequestRepository _requestRepository;
    private final IUserRepository _userRepository;
    private final IAgentService _agentService;
    private final IAuthorityRepository _authorityRepository;
    private final IPriceListRepository _priceListRepository;
    private final IRatingRepository _ratingRepository;

    private final Logger logger = LoggerFactory.getLogger(RequestService.class);

    public UserService(IRequestAdRepository requestAdRepository, IRequestRepository requestRepository, IUserRepository userRepository, IAgentService agentService, IAuthorityRepository authorityRepository, IPriceListRepository priceListRepository, IRatingRepository ratingRepository) {
        _requestAdRepository = requestAdRepository;
        _requestRepository = requestRepository;
        _userRepository = userRepository;
        _agentService = agentService;
        _authorityRepository = authorityRepository;
        _priceListRepository = priceListRepository;
        _ratingRepository = ratingRepository;
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
    public List<UserResponse> getAllCustomersAndAgents() {
        List<User> users = _userRepository.findAllByDeleted(false);
        List<User> customersAndAgents = new ArrayList<>();
        for(User user: users){
           if(user.getAdmin() == null){
               customersAndAgents.add(user);
           }
        }
        return customersAndAgents.stream()
                .map(u -> mapUserToUserResponse(u))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(UUID id) {
        User user = _userRepository.findOneById(id);
        user.setDeleted(true);
        _userRepository.save(user);
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

    @Override
    public List<SimpleUserRequests> getAllUserRequests(UUID id, RequestStatus requestStatus) {
        List<Request> requestList = new ArrayList<>();
        for (Request request : _requestRepository.findAll()) {
            if(request.getCustomer().getId().equals(id) && request.getStatus().equals(requestStatus)) {
                if(!requestList.contains(request)) {
                    requestList.add(request);
                }
            }
        }
        return mapToSimpleUserRequest(requestList);
    }

    @Override
    public Collection<SimpleUserRequests> payRequest(UUID userId, UUID resID) {
        Request request = _requestRepository.findOneById(resID);
        if(request.getStatus().equals(RequestStatus.RESERVED)) {
            request.setStatus(RequestStatus.PAID);
            _requestRepository.save(request);
        }

        _agentService.changeStatusOfRequests(request, RequestStatus.CHECKED, RequestStatus.CANCELED);
        logger.info("Request with id " + request.getId() + " has been payed by user with id: " + userId);
        return getAllUserRequests(userId, RequestStatus.RESERVED);
    }

    @Override
    public Collection<SimpleUserRequests> dropRequest(UUID userId, UUID resID) {
        Request request = _requestRepository.findOneById(resID);
        RequestStatus retStatus = request.getStatus();
        if(!request.getStatus().equals(RequestStatus.PAID)) {
            request.setStatus(RequestStatus.DROPPED);
            _requestRepository.save(request);
        }
        logger.info("Request with id " + request.getId() + " has been dropped");
        return getAllUserRequests(userId, retStatus);
    }

    @Override
    public List<UserResponse> getCustomers() {
        List<User> users = _userRepository.findAllByDeleted(false);
        return users.stream()
                .filter(user -> user.getUserRole().equals(UserRole.SIMPLE_USER))
                .map(user -> mapUserToUserResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponse> getPermissions(UUID userId) {
        User user = _userRepository.findOneById(userId);
        List<RoleResponse> roleListOfUser = new ArrayList<>();
        for (Authority authority : user.getRoles()) {
            RoleResponse roleResponse = mapAuthorityToPermissionResponse(authority);
            roleListOfUser.add(roleResponse);
        }
        return roleListOfUser;
    }

    @Override
    public List<UserDetailsResponse> getUsers() {
        List<User> userList = _userRepository.findAllByDeleted(false);
        List<UserDetailsResponse> userDetailsResponseList = new ArrayList<>();
        for (User user : userList) {
            if(!user.getUserRole().equals(UserRole.ADMIN)) {
                userDetailsResponseList.add(mapUserToUserDetailsResponse(user));
            }
        }

        return userDetailsResponseList;
    }

    @Override
    public List<UserDetailsResponse> deleteRole(Long roleId, UUID userId) {
        User user = _userRepository.findOneById(userId);
        Authority authority = _authorityRepository.findOneById(roleId);
        user.getRoles().remove(authority);
        _userRepository.save(user);
        return getUsers();
    }

    private UserDetailsResponse mapUserToUserDetailsResponse(User user) {
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.setId(user.getId());
        userDetailsResponse.setUsername(user.getUsername());
        userDetailsResponse.setUserRole(user.getUserRole().toString());
        setFirstAndLastName(userDetailsResponse, user);
        userDetailsResponse.setRoleList(getPermissions(user.getId()));
        return userDetailsResponse;
    }

    private void setFirstAndLastName(UserDetailsResponse userDetailsResponse, User user) {
        if(user.getAgent() != null) {
            userDetailsResponse.setName(user.getAgent().getName());
        } else {
            userDetailsResponse.setName(user.getSimpleUser().getFirstName() + " " + user.getSimpleUser().getLastName());
        }
    }

    private RoleResponse mapAuthorityToPermissionResponse(Authority authority) {
        List<String> permissionListOfRole = new ArrayList<>();
        for (Permission permission : authority.getPermissions()) {
            permissionListOfRole.add(permission.getName());
        }
        return new RoleResponse(authority.getId(), authority.getName(), permissionListOfRole);
    }

    private List<SimpleUserRequests> mapToSimpleUserRequest(List<Request> requestList) {
        List<SimpleUserRequests> simpleUserRequestList = new ArrayList<>();
        for (Request request : requestList) {
            SimpleUserRequests simpleUserRequests = new SimpleUserRequests();
            Agent agent = _requestAdRepository.findAllByRequest(request).get(0).getAd().getAgent();
            simpleUserRequests.setAgent(agent.getName());
            simpleUserRequests.setPickUpAddress(request.getPickUpAddress().getCity() + ", " + request.getPickUpAddress().getStreet() + " " + request.getPickUpAddress().getNumber());
            simpleUserRequests.setReceptionDate(request.getReceptionDate().toString());
            simpleUserRequests.setId(request.getId());
            simpleUserRequests.setRequestStatus(request.getStatus().toString());
            String ads = "";
            String description = "";
            for (RequestAd requestAd : _requestAdRepository.findAllByRequest(request)) {
                ads += requestAd.getAd().getCar().getCarModel().getCarBrand().getName() + " " + requestAd.getAd().getCar().getCarModel().getName() + ", ";
                description += "Ad: " + ads.substring(0, ads.length()-1) + " in period: " + requestAd.getPickUpDate() + " at " +
                        requestAd.getPickUpTime() + " to " + requestAd.getReturnDate() + " at " + requestAd.getReturnTime() + ", ";
            }
            ads = ads.substring(0, ads.length() -2);
            description = description.substring(0, description.length() - 2);
            simpleUserRequests.setDescription(description);
            simpleUserRequests.setAd(ads);
            simpleUserRequestList.add(simpleUserRequests);
        }

        return simpleUserRequestList;
    }

    private UsersAdsResponse makeUsersAdDTO(RequestAd requestAd){
        Ad ad = requestAd.getAd();
        UsersAdsResponse retVal = new UsersAdsResponse();
        List<PhotoResponse> photos = new ArrayList<PhotoResponse>();
        List<Rating> ratings = _ratingRepository.findAllByAd_Id(ad.getId());
        int sum = 0;
        for(Rating rating : ratings){
            sum += Integer.parseInt(rating.getGrade());
        }
        double avgRating = 0;
        if(ratings.size() != 0){
            avgRating = ((double)sum) / ratings.size();
        }
        PriceList priceList = _priceListRepository.findOneByAgentId(ad.getAgent().getId());
        int price = Integer.parseInt(priceList.getPrice1day())*Integer.parseInt(ad.getCoefficient());
        AdSearchResponse adDTO = new AdSearchResponse(ad.getId(), ad.isLimitedDistance(), ad.getSeats(), ad.isCdw(), ad.getCreationDate(), photos, price, avgRating);
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
        carDTO.setKilometersTraveled(ad.getCar().getKilometersTraveled());
        String allLocations = "";
        int i = 0;
        List<AddressDTO> fullLocations = new ArrayList<>();
        for(Address add : ad.getAgent().getAddress()){
            allLocations += add.getCity();
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(add.getId());
            addressDTO.setCity(add.getCity());
            addressDTO.setStreet(add.getStreet());
            addressDTO.setNumber(add.getNumber());
            fullLocations.add(addressDTO);
            if(ad.getAgent().getAddress().size() > 1){
                if(i < ad.getAgent().getAddress().size() - 1){
                    allLocations += ",";
                }
            }
            i++;
        }
        AgentSearchResponse agentDTO = new AgentSearchResponse(ad.getAgent().getId(), ad.getAgent().getSimpleUserId(), ad.getAgent().getName(), ad.getAgent().getDateFounded().toString(), allLocations, fullLocations);
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
