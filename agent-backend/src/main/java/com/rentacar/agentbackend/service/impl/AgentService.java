package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.RequestAd;
import com.rentacar.agentbackend.repository.IRequestAdRepository;
import com.rentacar.agentbackend.service.IAgentService;
import com.rentacar.agentbackend.util.enums.CarRequestStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"StringConcatenationInLoop", "unused"})
@Service
public class AgentService implements IAgentService {

    private final IRequestAdRepository _requestAdRepository;

    public AgentService(IRequestAdRepository requestAdRepository) {
        _requestAdRepository = requestAdRepository;
    }

    @Override
    public Collection<AgentRequests> getAllPendingRequests(UUID id) {
        List<Request> requestList = new ArrayList<>();
        for (RequestAd requestAd : _requestAdRepository.findAll()) {
            if(requestAd.getAd().getAgent().getId().equals(id) && requestAd.getRequest().getStatus().equals(CarRequestStatus.PENDING)) {
                if(!requestList.contains(requestAd.getRequest())) {
                    requestList.add(requestAd.getRequest());
                }
            }
        }
        return mapToAgentRequest(requestList);
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
