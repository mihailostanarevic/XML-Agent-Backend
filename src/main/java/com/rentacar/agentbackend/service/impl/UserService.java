package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.repository.IUserRepository;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UnknownFormatConversionException;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository _userRepository;

    public UserService(IUserRepository userRepository) {
        _userRepository = userRepository;
    }

    public User findOneByUsername(String mail) {
        return _userRepository.findOneByUsername(mail);
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
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setUserRole(user.getAuthorities().get(0).getAuthority());
        return userResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, UnknownFormatConversionException {
        User user = findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email "+username+"."));
        } else {
            return user;
        }
    }
}
