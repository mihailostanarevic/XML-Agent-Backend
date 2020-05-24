package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.repository.IUserRepository;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository _userRepository;

    public UserService(IUserRepository userRepository) {
        _userRepository = userRepository;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = _userRepository.findAllByDeleted(false);
        return users.stream()
                .map(user -> mapUserToUserResponse(user))
                .collect(Collectors.toList());
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setHasSignedIn(user.isHasSignedIn());
        if(user.getUserRole().equals(UserRole.AGENT)){
            userResponse.setId(user.getAgent().getId());
        }else if(user.getUserRole().equals(UserRole.SIMPLE_USER)){
            userResponse.setId(user.getSimpleUser().getId());
        }else if(user.getUserRole().equals(UserRole.ADMIN)){
            userResponse.setId(user.getAdmin().getId());
        }
        userResponse.setUsername(user.getUsername());
        userResponse.setUserRole(user.getUserRole());
        return userResponse;
    }
}
