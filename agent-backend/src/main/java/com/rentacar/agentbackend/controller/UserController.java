package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.dto.response.UsersAdsResponse;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

}
