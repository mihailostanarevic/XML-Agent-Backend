package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RequestBodyID;
import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.service.IAgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping(value="/agent")
public class AgentController {

    private final IAgentService _agentService;

    public AgentController(IAgentService agentService) {
        _agentService = agentService;
    }

    @GetMapping("/requests")
    public ResponseEntity<Collection<AgentRequests>> createRequest(@RequestBody RequestBodyID id){
        Collection<AgentRequests> agentRequests = _agentService.getAllPendingRequests(id.getId());
        return new ResponseEntity<>(agentRequests, HttpStatus.OK);
    }
}
