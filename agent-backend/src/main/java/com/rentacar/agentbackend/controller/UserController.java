package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RequestBodyID;
import com.rentacar.agentbackend.dto.response.*;
import com.rentacar.agentbackend.service.IAdService;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService _userService;
    private final IAdService _adService;

    public UserController(IUserService userService, IAdService adService) {
        _userService = userService;
        _adService = adService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() throws Exception{
        return _userService.getAllUsers();
    }

    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<UserResponse> getCustomers() {
        return _userService.getCustomers();
    }

    @GetMapping("/{id}/requests")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<UsersAdsResponse> usersAdsFromStatus(@PathVariable("id") UUID userId, @RequestParam("status") String string){
        String stringStatus = string.toUpperCase();
        RequestStatus status;
        switch (stringStatus){
            case "PAID" : status = RequestStatus.PAID;
                break;
            case "CANCELED" : status = RequestStatus.CANCELED;
                break;
            case "CONFIRMED" : status = RequestStatus.CONFIRMED;
                break;
            case "DENIED" : status = RequestStatus.DENIED;
                break;
            case "APPROVED" : status = RequestStatus.APPROVED;
                break;
            case "RESERVED" : status = RequestStatus.RESERVED;
                break;
            default: status = RequestStatus.PENDING;
        }

        return _userService.getUsersRequestFromStatus(userId,status);
    }

    //    @GetMapping("/requests")
    @GetMapping("/{id}/requests/{status}")
    @PreAuthorize("hasAuthority('READ_REQUEST')")
    public ResponseEntity<List<SimpleUserRequests>> usersRequestFromStatus(@PathVariable("id") UUID userId, @PathVariable("status") String status){
        List<SimpleUserRequests> simpleUserRequests;
        if(status.equalsIgnoreCase("PENDING")) {
            simpleUserRequests = _userService.getAllUserRequests(userId, RequestStatus.PENDING);
        } else if(status.equalsIgnoreCase("RESERVED")) {
            simpleUserRequests = _userService.getAllUserRequests(userId, RequestStatus.RESERVED);
        } else if(status.equalsIgnoreCase("PAID")) {
            simpleUserRequests = _userService.getAllUserRequests(userId, RequestStatus.PAID);
        } else if(status.equalsIgnoreCase("CHECKED")) {
            simpleUserRequests = _userService.getAllUserRequests(userId, RequestStatus.PAID);
        } else {
            simpleUserRequests = _userService.getAllUserRequests(userId, RequestStatus.CANCELED);
        }
        return new ResponseEntity<>(simpleUserRequests, HttpStatus.OK);
    }

    @PutMapping("/{id}/requests/{resID}/pay")
    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    public ResponseEntity<Collection<SimpleUserRequests>> userPay(@RequestBody RequestBodyID requestBodyID){
        return new ResponseEntity<>(_userService.payRequest(requestBodyID.getId(), requestBodyID.getRequestID()), HttpStatus.OK);
    }

    @PutMapping("/{id}/requests/{resID}/drop")
    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    public ResponseEntity<Collection<SimpleUserRequests>> userDrop(@RequestBody RequestBodyID requestBodyID){
        return new ResponseEntity<>(_userService.dropRequest(requestBodyID.getId(), requestBodyID.getRequestID()), HttpStatus.OK);
    }

    @GetMapping("/{id}/ads")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<AdResponse> getAd(@PathVariable UUID id) throws Exception{
        return _adService.getAgentAds(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CHANGE_PERMISSION')")
    public ResponseEntity<List<UserDetailsResponse>> getUsers() {
        return new ResponseEntity<>(_userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('CHANGE_PERMISSION')")
    public ResponseEntity<List<RoleResponse>> getPermissions(@PathVariable("id") UUID userId) {
        return new ResponseEntity<>(_userService.getPermissions(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('CHANGE_PERMISSION')")
    public ResponseEntity<List<UserDetailsResponse>> deleteRole(@PathVariable("userId") UUID userId,
                                                                @PathVariable("roleId") Long roleId) {
        return new ResponseEntity<>(_userService.deleteRole(roleId, userId), HttpStatus.OK);
    }

}
