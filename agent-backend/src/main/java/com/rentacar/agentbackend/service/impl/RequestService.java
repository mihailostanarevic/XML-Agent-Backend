package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.RequestDTO;
import com.rentacar.agentbackend.entity.Ad;
import com.rentacar.agentbackend.entity.Agent;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IRequestService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RequestService implements IRequestService {

    private final IRequestRepository _requestRepository;
    private final IAgentRepository _agentRepository;
    private final IAdRepository _adRepository;
    private final ISimpleUserRepository _simpleUserRepository;
    private final IAddressRepository _addressRepository;

    public RequestService(IRequestRepository requestRepository, IAgentRepository agentRepository, IAdRepository adRepository, ISimpleUserRepository simpleUserRepository, IAddressRepository addressRepository) {
        _requestRepository = requestRepository;
        _agentRepository = agentRepository;
        _adRepository = adRepository;
        _simpleUserRepository = simpleUserRepository;
        _addressRepository = addressRepository;
    }

    @Override
    public void processRequests(List<RequestDTO> requestList) {
        List<RequestDTO> processedList = new ArrayList<>();
        for (RequestDTO requestDTO : requestList) {
            Ad ad = _adRepository.findOneById(requestDTO.getAdID());
            Agent agent = _agentRepository.findOneById(ad.getAgent().getId());
            if(requestDTO.isBundle() && !processedList.contains(requestDTO)) {
                List<RequestDTO> bundleList = new ArrayList<>();
                for (RequestDTO agentRequest : requestList) {
                    Ad ad1 = _adRepository.findOneById(agentRequest.getAdID());
                    Agent agent1 = _agentRepository.findOneById(ad.getAgent().getId());
                    if(agent.equals(agent1) && !bundleList.contains(agentRequest) && agentRequest.isBundle()) {
                        bundleList.add(agentRequest);
                        processedList.add(agentRequest);
                    }
                }
                createBundleRequest(bundleList);
            } else if(!requestDTO.isBundle()) {
                createRequest(requestDTO);
            }
        }
    }

    @Override
    public Request createRequest(RequestDTO requestDTO) {
        Request request = new Request();
        Set<Ad> adSet = new HashSet<>();
        adSet.add(_adRepository.findOneById(requestDTO.getAdID()));
        request.setAds(adSet);
        createRequestWithoutCarID(request, requestDTO);
        _requestRepository.save(request);
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
        request.setAds(adSet);
        createRequestWithoutCarID(request, requestList.get(0));
        _requestRepository.save(request);
        return request;
    }
    private Request createRequestWithoutCarID(Request request, RequestDTO requestDTO) {
        request.setCustomer(_simpleUserRepository.findOneById(requestDTO.getCustomerID()));
        request.setStatus(RequestStatus.PENDING);
        request.setPickUpDate(LocalDate.parse(requestDTO.getPickUpDate()));
        request.setPickUpTime(LocalTime.parse(requestDTO.getPickUpTime()));
        request.setReturnDate(LocalDate.parse(requestDTO.getReturnDate()));
        request.setReturnTime(LocalTime.parse(requestDTO.getReturnTime()));
        request.setPickUpAddress(_addressRepository.findOneById(requestDTO.getPickUpAddress()));
        request.setDeleted(false);
        return request;
    }

}
