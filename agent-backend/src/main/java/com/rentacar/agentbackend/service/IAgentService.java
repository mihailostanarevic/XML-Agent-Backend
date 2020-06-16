package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.RequestAd;
import com.rentacar.agentbackend.util.enums.CarRequestStatus;
import com.rentacar.agentbackend.util.enums.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IAgentService {

    Collection<AgentRequests> getAllAgentRequests(UUID id, RequestStatus carRequestStatus);       // agent id

    Collection<AgentRequests> approveRequest(UUID agentId, UUID requestID);

    boolean checkRequestMatching(RequestAd requestFirst, RequestAd requestSecond);

    void changeStatusOfRequests(Request baseRequest, RequestStatus wakeUpStatus, RequestStatus finalStatus);
}
