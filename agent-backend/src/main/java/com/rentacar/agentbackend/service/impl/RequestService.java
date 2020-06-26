package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.RequestDTO;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IAgentService;
import com.rentacar.agentbackend.service.IRequestService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Service
public class RequestService implements IRequestService {

    private final IRequestRepository _requestRepository;
    private final IAgentRepository _agentRepository;
    private final IAdRepository _adRepository;
    private final ISimpleUserRepository _simpleUserRepository;
    private final IUserRepository _userRepository;
    private final IAuthorityRepository _authorityRepository;
    private final IAddressRepository _addressRepository;
    private final IRequestAdRepository _requestAdRepository;

    public RequestService(IRequestRepository requestRepository, IAgentRepository agentRepository, IAdRepository adRepository, ISimpleUserRepository simpleUserRepository, IUserRepository userRepository, IAuthorityRepository authorityRepository, IAddressRepository addressRepository, IRequestAdRepository requestAdRepository) {
        _requestRepository = requestRepository;
        _agentRepository = agentRepository;
        _adRepository = adRepository;
        _simpleUserRepository = simpleUserRepository;
        _userRepository = userRepository;
        _authorityRepository = authorityRepository;
        _addressRepository = addressRepository;
        _requestAdRepository = requestAdRepository;
    }

    @Override
    public void processRequests(List<RequestDTO> requestList) {
        List<RequestDTO> processedList = new ArrayList<>();
        for (RequestDTO requestDTO : requestList) {
            boolean canCreateBundle = true;
            Ad ad = _adRepository.findOneById(requestDTO.getAdID());
            Agent agent = _agentRepository.findOneById(ad.getAgent().getId());
            if (requestDTO.isBundle() && !processedList.contains(requestDTO)) {
                List<RequestDTO> bundleList = new ArrayList<>();
                for (RequestDTO agentRequest : requestList) {
                    Ad ad1 = _adRepository.findOneById(agentRequest.getAdID());
                    Agent agent1 = _agentRepository.findOneById(ad1.getAgent().getId());
                    if(agentRequest.isBundle() && agent.equals(agent1) && !bundleList.contains(agentRequest)) {
                        if (ad.isAvailable()) {
                            bundleList.add(agentRequest);
                            processedList.add(agentRequest);
                        } else {
                            canCreateBundle = false;
                        }
                    }
                }
                if(canCreateBundle) {
                    createBundleRequest(bundleList);
                }
            } else if (!requestDTO.isBundle()) {
                if (ad.isAvailable()) {
                    createRequest(requestDTO);
                }
            }
        }
    }

    /**
     * Check whether the car is available in that period
     * */
    private boolean checkCarAvailability(Ad ad, RequestDTO requestDTO) {
        List<RequestAd> requestAdList = _requestAdRepository.findAllByAd(ad);
        for (RequestAd requestAd : requestAdList) {
            boolean startEndDate = requestAd.getReturnDate().isBefore(LocalDate.parse(requestDTO.getPickUpDate()));
            if (!startEndDate) {
                boolean endStartDate = LocalDate.parse(requestDTO.getReturnDate()).isBefore(requestAd.getPickUpDate());
                if (!endStartDate) {
                    if(requestAd.getReturnDate().isEqual(LocalDate.parse(requestDTO.getPickUpDate()))) {
                        if (!requestAd.getReturnTime().isBefore(LocalTime.parse(requestDTO.getPickUpTime()))) {
                            return false;
                        }
                    }
                    else if(requestAd.getPickUpDate().isEqual(LocalDate.parse(requestDTO.getReturnDate()))) {
                        if (!requestAd.getPickUpTime().isAfter(LocalTime.parse(requestDTO.getReturnTime()))) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Request createRequest(RequestDTO requestDTO) {
        Request request = new Request();
        Set<Ad> adSet = new HashSet<>();
        adSet.add(_adRepository.findOneById(requestDTO.getAdID()));
        SimpleUser simpleUser;
        if(requestDTO.getCustomerID() != null) {
            simpleUser = _simpleUserRepository.findOneById(requestDTO.getCustomerID());
        } else {
            User user = _userRepository.findOneByUsername(requestDTO.getCustomerUsername());
            simpleUser = _simpleUserRepository.findOneByUser(user);
        }
        request.setCustomer(simpleUser);
        request.setStatus(RequestStatus.PENDING);
        request.setPickUpAddress(_addressRepository.findOneById(requestDTO.getPickUpAddress()));
        request.setDeleted(false);
        List<RequestDTO> requestDTOList = new ArrayList<>();
        requestDTOList.add(requestDTO);
        _requestRepository.save(request);
        createRequestAd(request, requestDTOList);
        User user = _userRepository.findOneById(simpleUser.getUser().getId());
        Authority authority = _authorityRepository.findByName("ROLE_REQUEST");
        Authority authority2 = _authorityRepository.findByName("ROLE_COMMENT_USER");
        Authority authority3 = _authorityRepository.findByName("ROLE_MESSAGE_USER");
        Authority authority4 = _authorityRepository.findByName("ROLE_REVIEWER_USER");
        user.getRoles().add(authority4);
        user.getRoles().add(authority3);
        user.getRoles().add(authority2);
        user.getRoles().add(authority);
        _userRepository.save(user);

        TimerTask taskPending = new TimerTask() {
            public void run() {
                System.out.println("Request performed on: " + LocalTime.now() + ", " +
                        "Request id: " + Thread.currentThread().getName());
                if(request.getStatus().equals(RequestStatus.PENDING)) {
                    request.setStatus(RequestStatus.CANCELED);
                    _requestRepository.save(request);
                }
            }
        };
        Timer timer = new Timer(request.getId().toString());
        long delay = (24 * 60 * 60 * 1000);
        System.out.println("Request received at: " + LocalTime.now());
        timer.schedule(taskPending, delay);

        return request;
    }

    @Override
    public Request createBundleRequest(List<RequestDTO> requestList) {
        Request request = new Request();
        Set<Ad> adSet = new HashSet<>();
        for (RequestDTO requestDTO : requestList) {
            Ad ad = _adRepository.findOneById(requestDTO.getAdID());
            adSet.add(ad);
        }
        request.setCustomer(_simpleUserRepository.findOneById(requestList.get(0).getCustomerID()));
        request.setStatus(RequestStatus.PENDING);
        request.setPickUpAddress(_addressRepository.findOneById(requestList.get(0).getPickUpAddress()));
        request.setDeleted(false);
        _requestRepository.save(request);
        createRequestAd(request, requestList);
        SimpleUser simpleUser;
        if(requestList.get(0).getCustomerID() != null) {
            simpleUser = _simpleUserRepository.findOneById(requestList.get(0).getCustomerID());
        } else {
            User user = _userRepository.findOneByUsername(requestList.get(0).getCustomerUsername());
            simpleUser = _simpleUserRepository.findOneByUser(user);
        }
        User user = _userRepository.findOneById(simpleUser.getUser().getId());
        Authority authority = _authorityRepository.findByName("ROLE_REQUEST");
        Authority authority2 = _authorityRepository.findByName("ROLE_COMMENT_USER");
        Authority authority3 = _authorityRepository.findByName("ROLE_MESSAGE_USER");
        Authority authority4 = _authorityRepository.findByName("ROLE_REVIEWER_USER");
        user.getRoles().add(authority4);
        user.getRoles().add(authority3);
        user.getRoles().add(authority2);
        user.getRoles().add(authority);
        _userRepository.save(user);
        TimerTask taskPending = new TimerTask() {
            public void run() {
                System.out.println("Bundle request performed on: " + LocalTime.now() + ", " +
                        "Request id: " + Thread.currentThread().getName());
                if(request.getStatus().equals(RequestStatus.PENDING)) {
                    request.setStatus(RequestStatus.CANCELED);
                    _requestRepository.save(request);
                }
            }
        };
        Timer timer = new Timer(request.getId().toString());
        long delay = (24 * 60 * 60 * 1000);
        System.out.println("Bundle received at: " + LocalTime.now());
        timer.schedule(taskPending, delay);
        return request;
    }

    private void createRequestAd(Request request, List<RequestDTO> requestDTOList) {
        for (RequestDTO requestDTO : requestDTOList) {
            RequestAd requestAd = new RequestAd();
            requestAd.setPickUpDate(LocalDate.parse(requestDTO.getPickUpDate()));
            requestAd.setPickUpTime(LocalTime.parse(requestDTO.getPickUpTime()));
            requestAd.setReturnDate(LocalDate.parse(requestDTO.getReturnDate()));
            requestAd.setReturnTime(LocalTime.parse(requestDTO.getReturnTime()));
            requestAd.setAd(_adRepository.findOneById(requestDTO.getAdID()));
            requestAd.setRequest(request);
            _requestAdRepository.save(requestAd);
        }
    }

    // napraviti novi request za taj ad
    // obrisati zahteve u kojima je taj oglas vec iznajmljen u to vreme
    @Override
    public void changeAdAvailability(RequestDTO request) {
        Ad requiredAd = _adRepository.findOneById(request.getAdID());
        for (RequestAd requestAd : _requestAdRepository.findAllByAd(requiredAd)) {
            if(!checkCarAvailability(requestAd.getAd(), request)) {
                requestAd.getRequest().setDeleted(true);
            }
        }
        createRequest(request);
    }

}
