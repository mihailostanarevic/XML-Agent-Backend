package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.*;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value="/auth")
public class AuthController extends ValidationControler {

    private final IAuthService _authService;

    public AuthController(IAuthService authService) {
        _authService = authService;
    }

    @GetMapping("/")
    public ResponseEntity<?> hello(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping("/create-agent")
    @PreAuthorize("hasAuthority('CREATE_AGENT')")
    public UserResponse createAgent(@RequestBody CreateAgentRequest request) throws Exception{
//        validateAgentJSON(request);
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
    public UserResponse newPassword(@PathVariable UUID id , @RequestBody NewPassordRequest request) throws Exception{
        return _authService.setNewPassword(id,request);
    }

    @PutMapping("/approve-registration-request")
    public void approveRegistrationRequest(@RequestBody GetIdRequest request) throws Exception{
        _authService.approveRegistrationRequest(request);
    }

    @PutMapping("/deny-registration-request")
    public void denyRegistrationRequest(@RequestBody GetIdRequest request) throws Exception{
        _authService.denyRegistrationRequest(request);
    }

    @GetMapping("/registration-requests")
    @PreAuthorize("hasAuthority('LOGIN')")
    public List<UserResponse> getAllRegistrationRequests() throws Exception{
        return _authService.getAllRegistrationRequests();
    }
}
