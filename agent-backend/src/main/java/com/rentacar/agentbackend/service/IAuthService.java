package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.*;
import com.rentacar.agentbackend.dto.response.RequestResponse;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.service.impl.GeneralException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface IAuthService {

    UserResponse createAgent(CreateAgentRequest request) throws Exception;

    UserResponse createSimpleUser(CreateSimpleUserRequest request) throws Exception;

    UserResponse login(LoginRequest request, HttpServletRequest httpServletRequest) throws Exception;

    UserResponse setNewPassword(UUID id, NewPassordRequest request) throws Exception;

    void confirmRegistrationRequest(GetIdRequest request) throws Exception;

    void approveRegistrationRequest(GetIdRequest request) throws Exception;

    void denyRegistrationRequest(GetIdRequest request) throws Exception;

    List<UserResponse> getAllRegistrationRequests() throws Exception;

    void checkSQLInjection(CreateAgentRequest request)throws GeneralException;

    RequestResponse limitRedirect(HttpServletRequest request);

    void forgottenPassword(ForgottenPasswordRequest request) throws Exception;
}
