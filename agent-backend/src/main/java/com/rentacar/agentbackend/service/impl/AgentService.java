package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.RequestAd;
import com.rentacar.agentbackend.repository.IRequestAdRepository;
import com.rentacar.agentbackend.repository.IRequestRepository;
import com.rentacar.agentbackend.service.IAgentService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@SuppressWarnings({"StringConcatenationInLoop", "unused"})
@Service
public class AgentService implements IAgentService {

    private final IRequestAdRepository _requestAdRepository;
    private final IRequestRepository _requestRepository;
    private final Logger logger = LoggerFactory.getLogger(AgentService.class);

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
    public Collection<AgentRequests> approveRequest(UUID agentId, UUID requestID) {
        Request request = _requestRepository.findOneById(requestID);
        request.setStatus(RequestStatus.RESERVED);
        logger.info("Status of request with id: " + request.getId() + " changed from " + request.getStatus() + " to " + RequestStatus.RESERVED);
        _requestRepository.save(request);

        changeStatusOfRequests(request, RequestStatus.PENDING, RequestStatus.CHECKED);

        TimerTask taskPaid = new TimerTask() {
            public void run() {
                System.out.println("Approved request performed on: " + LocalTime.now() + ", " +
                        "Request id: " + Thread.currentThread().getName());
                if(!request.getStatus().equals(RequestStatus.PAID)) {
                    logger.info("Status of request with id: " + request.getId() + " changed from " + request.getStatus() + " to " + RequestStatus.CANCELED);
                    request.setStatus(RequestStatus.CANCELED);
                    _requestRepository.save(request);

                    changeStatusOfRequests(request, RequestStatus.CHECKED, RequestStatus.PENDING);
                }
            }
        };
        Timer timer = new Timer(request.getId().toString());
        long delay = (12 * 60 * 60 * 1000);
        System.out.println("Approved request received at: " + LocalTime.now());
        timer.schedule(taskPaid, delay);
        return getAllAgentRequests(agentId, RequestStatus.PENDING);
    }

    public void changeStatusOfRequests(Request baseRequest, RequestStatus wakeUpStatus, RequestStatus finalStatus) {
        for (RequestAd requestAd : _requestAdRepository.findAllByRequest(baseRequest)) {
            for (RequestAd requestAdAll : _requestAdRepository.findAll()) {
                if (requestAdAll.getRequest().getStatus().equals(wakeUpStatus)
                        && checkRequestMatching(requestAd, requestAdAll)) {
                    requestAdAll.getRequest().setStatus(finalStatus);
                    _requestRepository.save(requestAdAll.getRequest());
                }
            }
        }
    }

    @Override
    public Collection<AgentRequests> denyRequest(UUID agentId, UUID requestID) {
        Request request = _requestRepository.findOneById(requestID);
        request.setStatus(RequestStatus.CANCELED);
        _requestRepository.save(request);

        changeStatusOfRequests(request, RequestStatus.CHECKED, RequestStatus.PENDING);
        return getAllAgentRequests(agentId, RequestStatus.PENDING);
    }

    public boolean checkRequestMatching(RequestAd requestFirst, RequestAd requestSecond) {
        if(requestFirst.getAd().getId().equals(requestSecond.getAd().getId())) {
            if (requestFirst.getReturnDate().isBefore(requestSecond.getPickUpDate())) {
                return false;
            } else {
                if (requestFirst.getPickUpDate().isAfter(requestSecond.getReturnDate())) {
                    return false;
                } else {
                    if (requestFirst.getReturnDate().isEqual(requestSecond.getPickUpDate())) {
                        return requestFirst.getReturnTime().isAfter(requestSecond.getPickUpTime());
                    } else if (requestSecond.getReturnDate().isEqual(requestFirst.getPickUpDate())) {
                        return requestFirst.getPickUpTime().isBefore(requestSecond.getReturnTime());
                    } else {
                        return true;
                    }
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
            agentRequest.setRequestStatus(request.getStatus().toString());
            String ads = "";
            String description = "";
            for (RequestAd requestAd : _requestAdRepository.findAllByRequest(request)) {
                ads += requestAd.getAd().getCar().getCarModel().getCarBrand().getName() + " " + requestAd.getAd().getCar().getCarModel().getName() + ",";
                description += "Ad: " + ads.substring(0, ads.length()-1) + " in period: " + requestAd.getPickUpDate() + " at " +
                        requestAd.getPickUpTime() + " to " + requestAd.getReturnDate() + " at " + requestAd.getReturnTime() + ", ";
            }
            description = description.substring(0, description.length() - 2);
            ads = ads.substring(0, ads.length() -1);
            agentRequest.setDescription(description);
            agentRequest.setAd(ads);
            agentRequestList.add(agentRequest);
        }

        return agentRequestList;
    }
}
