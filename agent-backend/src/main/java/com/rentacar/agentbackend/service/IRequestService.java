package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.RequestDTO;
import com.rentacar.agentbackend.entity.Request;

import java.util.List;

public interface IRequestService {

    void processRequests(List<RequestDTO> requestList);

    Request createRequest(RequestDTO requestDTO);

    Request createBundleRequest(List<RequestDTO> requestList);

}
