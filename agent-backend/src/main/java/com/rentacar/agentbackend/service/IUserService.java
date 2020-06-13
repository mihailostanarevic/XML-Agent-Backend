package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.dto.response.UsersAdsResponse;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.util.enums.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    List<UserResponse> getAllUsers();
    User getUser(UUID id);
    List<UsersAdsResponse> getUsersAdsFromStatus(UUID id, RequestStatus status);
}
