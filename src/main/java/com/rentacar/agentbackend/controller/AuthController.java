package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateAgentRequest;
import com.rentacar.agentbackend.dto.request.CreateSimpleUserRequest;
import com.rentacar.agentbackend.dto.request.LoginRequest;
import com.rentacar.agentbackend.dto.request.NewPasswordRequest;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.security.TokenUtils;
import com.rentacar.agentbackend.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenUtils tokenUtils;

    private final IAuthService _authService;

    public AuthController(IAuthService authService) {
        _authService = authService;
    }

    @PostMapping("/create-agent")
    public UserResponse createAgent(@RequestBody CreateAgentRequest request) throws Exception{
        return _authService.createAgent(request);
    }

    @PostMapping("/create-simple-user")
    public UserResponse createSimpleUser(@RequestBody CreateSimpleUserRequest request) throws Exception{
        return _authService.createSimpleUser(request);
    }

    @PutMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) throws Exception{
        return _authService.login(request);
    }

    @PutMapping("/{id}/new-password")
    public UserResponse newPassword(@PathVariable UUID id, @RequestBody NewPasswordRequest request) throws Exception{
        return _authService.setNewPassword(id, request);
    }

    @PutMapping("/approve/{id}/registration-request")
    public void approveRegistrationRequest(@PathVariable UUID id) throws Exception{
        _authService.approveRegistrationRequest(id);
    }

    @PutMapping("/deny/{id}/registration-request")
    public void denyRegistrationRequest(@PathVariable UUID id) throws Exception{
        _authService.denyRegistrationRequest(id);
    }

    @GetMapping("/registration-requests")
    @PreAuthorize("hasRole('SIMPLE_USER')")
    public List<UserResponse> getAllRegistrationRequests() throws Exception{
        return _authService.getAllRegistrationRequests();
    }
}
