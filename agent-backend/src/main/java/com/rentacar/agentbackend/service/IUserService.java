package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.SimpleUserRequests;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.dto.response.UsersAdsResponse;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.util.enums.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IUserService {

    List<UserResponse> getAllUsers();

    List<UserResponse> getAllCustomersAndAgents();

    void deleteUser(UUID id);

    User getUser(UUID id);

    List<SimpleUserRequests> getAllUserRequests(UUID id, RequestStatus reserved);

    Collection<SimpleUserRequests> payRequest(UUID userId, UUID resID);

    List<UsersAdsResponse> getUsersRequestFromStatus(UUID id, RequestStatus status);

    Collection<SimpleUserRequests> dropRequest(UUID id, UUID requestID);

    List<UserResponse> getCustomers();
}
