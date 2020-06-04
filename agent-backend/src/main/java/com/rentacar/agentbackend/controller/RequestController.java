package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RequestDTO;
import com.rentacar.agentbackend.service.IRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rent-request")
public class RequestController {

    private final IRequestService _requestService;

    public RequestController(IRequestService requestService) {
        _requestService = requestService;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createRequest(@RequestBody List<RequestDTO> requestList){
        _requestService.processRequests(requestList);
        return new ResponseEntity<>("Hello from Rent service", HttpStatus.OK);
    }

}
