package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.ApproveDenyAccessoryRequest;

public interface IMessageCarAccessoryService {

    void approveDenyAccessory(ApproveDenyAccessoryRequest request);
}
