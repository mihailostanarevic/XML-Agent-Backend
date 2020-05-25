package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.UserResponse;

import java.util.List;

public interface IUserService {

    List<UserResponse> getAllUsers();
}
