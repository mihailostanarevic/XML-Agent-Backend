package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.entity.Request;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IAgentService {

    Collection<AgentRequests> getAllPendingRequests(UUID id);       // agent id

}
