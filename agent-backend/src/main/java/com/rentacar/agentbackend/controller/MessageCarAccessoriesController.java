package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.ApproveDenyAccessoryRequest;
import com.rentacar.agentbackend.service.IMessageCarAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message-car-accessories")
public class MessageCarAccessoriesController {

    @Autowired
    private IMessageCarAccessoryService _messageCarAccessoriesService;

    @PutMapping
    @PreAuthorize("hasAuthority('RECEIVE_MESSAGE')")
    public void approveOrDeny(@RequestBody ApproveDenyAccessoryRequest request){
        System.out.println(request.getId());
        System.out.println(request.isApproved());
        _messageCarAccessoriesService.approveDenyAccessory(request);
    }
}
