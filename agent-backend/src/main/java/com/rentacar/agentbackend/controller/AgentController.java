package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RequestsSimpleUser;
import com.rentacar.agentbackend.dto.response.AgentRequests;
import com.rentacar.agentbackend.service.IAgentService;
import com.rentacar.agentbackend.util.enums.CarRequestStatus;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}/requests/{status}")
    @PreAuthorize("hasAuthority('READ_REQUEST')")
    public ResponseEntity<Collection<AgentRequests>> getAllRequests(@PathVariable("id") UUID userId, @PathVariable("status") String status){
        RequestStatus carRequestStatus;
        if(status.equalsIgnoreCase("PENDING")) {
            carRequestStatus = RequestStatus.PENDING;
        } else if(status.equalsIgnoreCase("RESERVED")) {
            carRequestStatus = RequestStatus.RESERVED;
        } else if(status.equalsIgnoreCase("PAID")) {
            carRequestStatus = RequestStatus.PAID;
        } else {
            carRequestStatus = RequestStatus.CANCELED;
        }
        Collection<AgentRequests> agentRequests = _agentService.getAllAgentRequests(userId, carRequestStatus);
        return new ResponseEntity<>(agentRequests, HttpStatus.OK);
    }

    @GetMapping("/{id}/requests/{resID}/approve")
    @PreAuthorize("hasAuthority('APPROVE_REQUEST')")
    public ResponseEntity<?> approveRequest(@PathVariable("id") UUID agentId, @PathVariable("resID") UUID reqID){
        return new ResponseEntity<>(_agentService.approveRequest(agentId, reqID), HttpStatus.OK);
    }

}
