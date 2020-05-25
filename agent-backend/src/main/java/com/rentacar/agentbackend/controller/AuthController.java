package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateAgentRequest;
import com.rentacar.agentbackend.dto.request.CreateSimpleUserRequest;
import com.rentacar.agentbackend.dto.request.LoginRequest;
import com.rentacar.agentbackend.dto.request.NewPassordRequest;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService _authService;

    public AuthController(IAuthService authService) {
        _authService = authService;
    }

    @GetMapping("/hello")
    public void hello(){
        System.out.println("12312312312");
        new ResponseEntity<>("Hello from auth", HttpStatus.OK);
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
    public UserResponse newPassword(@PathVariable UUID id, @RequestBody NewPassordRequest request) throws Exception{
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
    public List<UserResponse> getAllRegistrationRequests() throws Exception{
        return _authService.getAllRegistrationRequests();
    }
}
