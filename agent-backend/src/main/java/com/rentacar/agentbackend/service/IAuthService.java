package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreateAgentRequest;
import com.rentacar.agentbackend.dto.request.CreateSimpleUserRequest;
import com.rentacar.agentbackend.dto.request.LoginRequest;
import com.rentacar.agentbackend.dto.request.NewPassordRequest;
import com.rentacar.agentbackend.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface IAuthService {

    UserResponse createAgent(CreateAgentRequest request) throws Exception;

    UserResponse createSimpleUser(CreateSimpleUserRequest request) throws Exception;

    UserResponse login(LoginRequest request) throws Exception;

    UserResponse setNewPassword(UUID id, NewPassordRequest request) throws Exception;

    void approveRegistrationRequest(UUID id) throws Exception;

    void denyRegistrationRequest(UUID id) throws Exception;

    List<UserResponse> getAllRegistrationRequests() throws Exception;
}
