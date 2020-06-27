package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.*;
import com.rentacar.agentbackend.dto.response.RequestResponse;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    // permit all
    @PostMapping("/create-simple-user")
    public UserResponse createSimpleUser(@RequestBody CreateSimpleUserRequest request) throws Exception{
        return _authService.createSimpleUser(request);
    }

    // permit all
    @PutMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) throws Exception{
        return _authService.login(request, httpServletRequest);
    }

    @PutMapping("/{id}/new-password")
    @PreAuthorize("hasAuthority('LOGIN')")
    public UserResponse newPassword(@PathVariable UUID id , @RequestBody NewPassordRequest request) throws Exception{
        return _authService.setNewPassword(id,request);
    }

    @PutMapping("/confirm-registration-request")
    @PreAuthorize("hasAuthority('CREATE_AGENT')")
    public void confirmRegistrationRequest(@RequestBody GetIdRequest request) throws Exception{
        _authService.confirmRegistrationRequest(request);
    }

    @PutMapping("/approve-registration-request")
    public void approveRegistrationRequest(@RequestBody GetIdRequest request) throws Exception{
        _authService.approveRegistrationRequest(request);
    }

    @PutMapping("/deny-registration-request")
    @PreAuthorize("hasAuthority('DENY_AGENT')")
    public void denyRegistrationRequest(@RequestBody GetIdRequest request) throws Exception{
        _authService.denyRegistrationRequest(request);
    }

    @GetMapping("/registration-requests")
    @PreAuthorize("hasAuthority('CREATE_AGENT')")
    public List<UserResponse> getAllRegistrationRequests() throws Exception{
        return _authService.getAllRegistrationRequests();
    }

    @GetMapping("/logging-limit")
    public RequestResponse loggingLimit(HttpServletRequest request){
        return _authService.limitRedirect(request);
    }

    @PutMapping("/forgotten-password")
    public void forgottenPassword(@RequestBody ForgottenPasswordRequest request) throws Exception{
        _authService.forgottenPassword(request);
    }
}
