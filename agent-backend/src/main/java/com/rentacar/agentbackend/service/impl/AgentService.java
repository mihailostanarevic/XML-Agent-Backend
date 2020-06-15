package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.RequestAd;
import com.rentacar.agentbackend.repository.IRequestAdRepository;
import com.rentacar.agentbackend.repository.IRequestRepository;
import com.rentacar.agentbackend.service.IAgentService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"StringConcatenationInLoop", "unused"})
@Service
public class AgentService implements IAgentService {

    private final IRequestAdRepository _requestAdRepository;
    private final IRequestRepository _requestRepository;

    public AgentService(IRequestAdRepository requestAdRepository, IRequestRepository requestRepository) {
        _requestAdRepository = requestAdRepository;
        _requestRepository = requestRepository;
    }

    @Override
    public Collection<AgentRequests> getAllAgentRequests(UUID id, RequestStatus carRequestStatus) {
        List<Request> requestList = new ArrayList<>();
        for (RequestAd requestAd : _requestAdRepository.findAll()) {
            if(requestAd.getAd().getAgent().getId().equals(id) && requestAd.getRequest().getStatus().equals(carRequestStatus)) {
                if(!requestList.contains(requestAd.getRequest())) {
                    requestList.add(requestAd.getRequest());
                }
            }
        }
        return mapToAgentRequest(requestList);
    }

    @Override
    public String approveRequest(UUID agentId, UUID requestID) {
        Request request = _requestRepository.findOneById(requestID);
        request.setStatus(RequestStatus.RESERVED);
        _requestRepository.save(request);

        for (RequestAd requestAd : _requestAdRepository.findAllByRequest(request)) {
            for (RequestAd requestAdAll : _requestAdRepository.findAll()) {
                if (requestAdAll.getRequest().getStatus().equals(RequestStatus.PENDING) &&
                        checkRequestMatching(requestAd, requestAdAll)) {
                    requestAdAll.getRequest().setStatus(RequestStatus.CANCELED);
                    _requestRepository.save(requestAdAll.getRequest());
                }
            }
        }
        return "Successfuly reserve this request and denied other matchers!";
    }

    private boolean checkRequestMatching(RequestAd requestFirst, RequestAd requestSecond) {
        if(requestFirst.getReturnDate().isBefore(requestSecond.getPickUpDate())) {
            return false;
        } else {
            if(requestFirst.getPickUpDate().isAfter(requestSecond.getReturnDate())) {
                return false;
            } else {
                if(requestFirst.getReturnDate().isEqual(requestSecond.getPickUpDate())) {
                    if (requestFirst.getReturnTime().isAfter(requestSecond.getPickUpTime())) {
                        return true;
                    }
                } else if(requestSecond.getReturnDate().isEqual(requestFirst.getPickUpDate())) {
                    if (requestFirst.getPickUpTime().isBefore(requestSecond.getReturnTime())) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    private Collection<AgentRequests> mapToAgentRequest(List<Request> requestList) {
        List<AgentRequests> agentRequestList = new ArrayList<>();
        for (Request request : requestList) {
            AgentRequests agentRequest = new AgentRequests();
            agentRequest.setCustomer(request.getCustomer().getFirstName() + " " + request.getCustomer().getLastName());
            agentRequest.setPickUpAddress(request.getPickUpAddress().getCity() + ", " + request.getPickUpAddress().getStreet() + " " + request.getPickUpAddress().getNumber());
            agentRequest.setReceptionDate(request.getReceptionDate().toString());
            agentRequest.setId(request.getId());
            String ads = "";
            for (RequestAd requestAd : _requestAdRepository.findAllByRequest(request)) {
                ads += requestAd.getAd().getCar().getCarModel().getCarBrand().getName() + " " + requestAd.getAd().getCar().getCarModel().getName() + ",";
            }
            ads = ads.substring(0, ads.length() -1);
            agentRequest.setAd(ads);
            agentRequestList.add(agentRequest);
        }

        return agentRequestList;
    }
}
