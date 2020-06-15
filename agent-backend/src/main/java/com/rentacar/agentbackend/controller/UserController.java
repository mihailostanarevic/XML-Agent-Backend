package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RequestsSimpleUser;
import com.rentacar.agentbackend.dto.response.SimpleUserRequests;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.dto.response.UsersAdsResponse;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.CarRequestStatus;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService _userService;

    public UserController(IUserService userService) {
        _userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() throws Exception{
        return _userService.getAllUsers();
    }

    @GetMapping("/{id}/ads")
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
            default: status = RequestStatus.PENDING;
        }

        return _userService.getUsersAdsFromStatus(userId,status);
    }

    // @GetMapping("/{id}/requests")
    @GetMapping("/requests")
    public ResponseEntity<List<SimpleUserRequests>> usersRequestFromStatus(@RequestBody RequestsSimpleUser requestsSimpleUser){
        List<SimpleUserRequests> simpleUserRequests;
        if(requestsSimpleUser.getRequestStatus().equalsIgnoreCase("PENDING")) {
            simpleUserRequests = _userService.getAllUserRequests(requestsSimpleUser.getId(), CarRequestStatus.PENDING);
        } else if(requestsSimpleUser.getRequestStatus().equalsIgnoreCase("RESERVED")) {
            simpleUserRequests = _userService.getAllUserRequests(requestsSimpleUser.getId(), CarRequestStatus.RESERVED);
        } else if(requestsSimpleUser.getRequestStatus().equalsIgnoreCase("PAID")) {
            simpleUserRequests = _userService.getAllUserRequests(requestsSimpleUser.getId(), CarRequestStatus.PAID);
        } else {
            simpleUserRequests = _userService.getAllUserRequests(requestsSimpleUser.getId(), CarRequestStatus.CANCELED);
        }
        return new ResponseEntity<>(simpleUserRequests, HttpStatus.OK);
    }
}
