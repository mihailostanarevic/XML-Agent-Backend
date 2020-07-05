package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.*;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.util.enums.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IUserService {

    List<UserResponse> getAllUsers();

    User getUser(UUID id);

    List<SimpleUserRequests> getAllUserRequests(UUID id, RequestStatus reserved);

    Collection<SimpleUserRequests> payRequest(UUID userId, UUID resID);

    List<UsersAdsResponse> getUsersRequestFromStatus(UUID id, RequestStatus status);

    Collection<SimpleUserRequests> dropRequest(UUID id, UUID requestID);

    List<UserResponse> getCustomers();

    List<RoleResponse> getPermissions(UUID userId);

    List<UserDetailsResponse> getUsers();

    List<UserDetailsResponse> deleteRole(Long roleId, UUID userId);
}
