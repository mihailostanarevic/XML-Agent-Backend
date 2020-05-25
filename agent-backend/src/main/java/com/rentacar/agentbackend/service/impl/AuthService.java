package com.rentacar.agentbackend.service.impl;

import com.github.rkpunjal.sqlsafe.SqlSafeUtil;
import com.rentacar.agentbackend.dto.request.CreateAgentRequest;
import com.rentacar.agentbackend.dto.request.CreateSimpleUserRequest;
import com.rentacar.agentbackend.dto.request.LoginRequest;
import com.rentacar.agentbackend.dto.request.NewPassordRequest;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.entity.Admin;
import com.rentacar.agentbackend.entity.Agent;
import com.rentacar.agentbackend.entity.SimpleUser;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.repository.IAdminRepository;
import com.rentacar.agentbackend.repository.IAgentRepository;
import com.rentacar.agentbackend.repository.ISimpleUserRepository;
import com.rentacar.agentbackend.repository.IUserRepository;
import com.rentacar.agentbackend.service.IAuthService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthService {

    private final PasswordEncoder _passwordEncoder;

    private final IUserRepository _userRepository;

    private final IAgentRepository _agentRepository;

    private final ISimpleUserRepository _simpleUserRepository;

    private final IAdminRepository _adminRepository;

    public AuthService(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAgentRepository agentRepository, ISimpleUserRepository simpleUserRepository, IAdminRepository adminRepository) {
        _passwordEncoder = passwordEncoder;
        _userRepository = userRepository;
        _agentRepository = agentRepository;
        _simpleUserRepository = simpleUserRepository;
        _adminRepository = adminRepository;
    }

    /**
     * checkSQLInjection prevent sql injection
     */
    @Override
    public void checkSQLInjection(CreateAgentRequest request)throws GeneralException {
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getUsername()))
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getPassword()))
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getRePassword()))
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getName()))
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getTin()))
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getBankAccountNumber()))
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserResponse createAgent(CreateAgentRequest request) throws Exception {
        if(!request.getPassword().equals(request.getRePassword())){
            throw new Exception("Passwords don't match.");
        }
        checkSQLInjection(request);
        User user = new User();
        Agent agent = new Agent();
        user.setDeleted(false);
        user.setHasSignedIn(false);
        user.setUsername(request.getUsername());
        user.setPassword(_passwordEncoder.encode(request.getPassword()));
        user.setUserRole(UserRole.AGENT);
        agent.setName(request.getName());
        agent.setBankAccountNumber(request.getBankAccountNumber());
        agent.setDateFounded(request.getDateFounded());
        agent.setTin(request.getTin());
        agent.setRequestStatus(RequestStatus.PENDING);
        Agent savedAgent = _agentRepository.save(agent);
        savedAgent.setUser(user);
        user.setAgent(savedAgent);
        User savedUser = _userRepository.save(user);

        return mapUserToUserResponse(savedUser);
    }

    @Override
    public UserResponse createSimpleUser(CreateSimpleUserRequest request) throws Exception {
        if(!request.getPassword().equals(request.getRePassword())){
            throw new Exception("Passwords don't match.");
        }
        User user = new User();
        SimpleUser simpleUser = new SimpleUser();
        user.setDeleted(false);
        user.setHasSignedIn(false);
        user.setUsername(request.getUsername());
        user.setPassword(_passwordEncoder.encode(request.getPassword()));
        user.setUserRole(UserRole.SIMPLE_USER);
        simpleUser.setAddress(request.getAddress());
        simpleUser.setCity(request.getCity());
        simpleUser.setCountry(request.getCountry());
        simpleUser.setFistName(request.getFistName());
        simpleUser.setLastName(request.getLastName());
        simpleUser.setSsn(request.getSsn());
        simpleUser.setRequestStatus(RequestStatus.PENDING);
        SimpleUser savedSimpleUser = _simpleUserRepository.save(simpleUser);
        savedSimpleUser.setUser(user);
        user.setSimpleUser(savedSimpleUser);
        User savedUser = _userRepository.save(user);

        return mapUserToUserResponse(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest request) throws Exception {
        User user = _userRepository.findOneByUsername(request.getUsername());

        if (user == null) {
            throw new Exception(String.format("Bad credentials."));
        }

        if (!_passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Bad credentials.");
        }

        if(user.isDeleted()){
            throw new Exception("Your account has been deleted.");
        }

        if(user.getUserRole().equals(UserRole.AGENT)){
            if(user.getAgent().getRequestStatus().equals(RequestStatus.PENDING)){
                throw new Exception("Your registration request hasn't been approved yet.");
            }
        }

        if(user.getUserRole().equals(UserRole.SIMPLE_USER)){
            if(user.getSimpleUser().getRequestStatus().equals(RequestStatus.PENDING)){
                throw new Exception("Your registration request hasn't been approved yet.");
            }
        }

        if(user.getUserRole().equals(UserRole.AGENT)){
            if(user.getAgent().getRequestStatus().equals(RequestStatus.DENIED)){
                throw new Exception("Your registration request has been denied.");
            }
        }

        if(user.getUserRole().equals(UserRole.SIMPLE_USER)){
            if(user.getSimpleUser().getRequestStatus().equals(RequestStatus.DENIED)){
                throw new Exception("Your registration request has been denied.");
            }
        }

        if(!user.isHasSignedIn()){
            user.setHasSignedIn(true);
            _userRepository.save(user);
        }

        return mapUserToUserResponse(user);
    }

    @Override
    public UserResponse setNewPassword(UUID id, NewPassordRequest request) throws Exception {
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new Exception("Passwords don't match");
        }

        Admin admin = _adminRepository.findOneById(id);
        Agent agent = _agentRepository.findOneById(id);
        SimpleUser simpleUser = _simpleUserRepository.findOneById(id);

        User user = null;

        if(admin != null){
            user = admin.getUser();
        }else if(agent != null){
            user = agent.getUser();
        }else if(simpleUser != null) {
            user = simpleUser.getUser();
        }

        user.setPassword(_passwordEncoder.encode(request.getPassword()));

        if(!user.isHasSignedIn()){
            user.setHasSignedIn(true);
        }

        if(admin != null){
            _adminRepository.save(admin);
        }

        return mapUserToUserResponse(user);
    }

    @Override
    public void approveRegistrationRequest(UUID id) throws Exception {
        Agent agent = _agentRepository.findOneById(id);
        SimpleUser simpleUser = _simpleUserRepository.findOneById(id);
        if(simpleUser == null){
            agent.setRequestStatus(RequestStatus.APPROVED);
            _agentRepository.save(agent);
        }else if(agent == null){
            simpleUser.setRequestStatus(RequestStatus.APPROVED);
            _simpleUserRepository.save(simpleUser);
        }
    }

    @Override
    public void denyRegistrationRequest(UUID id) throws Exception {
        Agent agent = _agentRepository.findOneById(id);
        SimpleUser simpleUser = _simpleUserRepository.findOneById(id);
        if(simpleUser == null){
            agent.setRequestStatus(RequestStatus.DENIED);
            _agentRepository.save(agent);
        }else if(agent == null){
            simpleUser.setRequestStatus(RequestStatus.DENIED);
            _simpleUserRepository.save(simpleUser);
        }
    }

    @Override
    public List<UserResponse> getAllRegistrationRequests() throws Exception {
        List<User> users = _userRepository.findAllByDeleted(false);
        List<User> requests = new ArrayList<>();
        for (User u: users){
            if(u.getUserRole().equals(UserRole.AGENT)){
                if(u.getAgent().getRequestStatus().equals(RequestStatus.PENDING)){
                    requests.add(u);
                }
            }else if(u.getUserRole().equals(UserRole.SIMPLE_USER)){
                if(u.getSimpleUser().getRequestStatus().equals(RequestStatus.PENDING)){
                    requests.add(u);
                }
            }
        }
//        if(requests.isEmpty()){
//            throw new Exception("There are no registration requests.");
//        }
        return requests.stream()
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
