package com.rentacar.agentbackend.service.impl;

import com.github.rkpunjal.sqlsafe.SqlSafeUtil;
import com.rentacar.agentbackend.dto.request.CreateAgentRequest;
import com.rentacar.agentbackend.dto.request.CreateSimpleUserRequest;
import com.rentacar.agentbackend.dto.request.LoginRequest;
import com.rentacar.agentbackend.dto.request.NewPassordRequest;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.security.TokenUtils;
import com.rentacar.agentbackend.service.IAuthService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
@Transactional
public class AuthService implements IAuthService {

    private final AuthenticationManager _authenticationManager;

    private final TokenUtils _tokenUtils;

    private final PasswordEncoder _passwordEncoder;

    private final IUserRepository _userRepository;

    private final IAgentRepository _agentRepository;

    private final ISimpleUserRepository _simpleUserRepository;

    private final IAdminRepository _adminRepository;

    @Autowired
    private IAuthorityRepository _authorityRepository;

    public AuthService(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAgentRepository agentRepository, ISimpleUserRepository simpleUserRepository, IAdminRepository adminRepository, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        _passwordEncoder = passwordEncoder;
        _userRepository = userRepository;
        _agentRepository = agentRepository;
        _simpleUserRepository = simpleUserRepository;
        _adminRepository = adminRepository;
        _authenticationManager = authenticationManager;
        _tokenUtils = tokenUtils;
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
        if(_userRepository.findOneByUsername(request.getUsername()) != null){
            throw new GeneralException("User with this username already exist", HttpStatus.BAD_REQUEST);
        }
        checkSQLInjection(request);
        User user = new User();
        Agent agent = new Agent();
        user.setDeleted(false);
        user.setHasSignedIn(false);
        user.setUsername(request.getUsername());
        user.setPassword(_passwordEncoder.encode(request.getPassword()));
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
            throw new GeneralException("Passwords don't match.", HttpStatus.BAD_REQUEST);
        }
        if(_userRepository.findOneByUsername(request.getUsername()) != null){
            throw new GeneralException("User with this username already exist", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        SimpleUser simpleUser = new SimpleUser();
        user.setDeleted(false);
        user.setHasSignedIn(false);
        user.setUsername(request.getUsername());
        user.setPassword(_passwordEncoder.encode(request.getPassword()));
        List<Authority> authorities = new ArrayList<>();
        authorities.add(_authorityRepository.findByName("ROLE_SIMPLE_USER"));
        authorities.add(_authorityRepository.findByName("ROLE_RENT_USER"));
        user.setAuthorities(new HashSet<>(authorities));
        simpleUser.setAddress(request.getAddress());
        simpleUser.setCity(request.getCity());
        simpleUser.setCountry(request.getCountry());
        simpleUser.setFirstName(request.getFirstName());
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

        String mail = request.getUsername();
        String password = request.getPassword();
        Authentication authentication = null;
        try {
            authentication = _authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(mail, password));
        }catch (BadCredentialsException e){
            throw new GeneralException("Bad credentials.", HttpStatus.BAD_REQUEST);
        }catch (DisabledException e){
            throw new GeneralException("Your registration request hasn't been approved yet.", HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            System.out.println("Neki drugi exception [Exception]");
            e.printStackTrace();
        }

        String jwt = "";
        int expiresIn = 0;
        if(!user.isHasSignedIn()){
            UserDetailsImpl userLog = (UserDetailsImpl) authentication.getPrincipal();
            jwt = _tokenUtils.generateToken(userLog.getUsername());
            expiresIn = _tokenUtils.getExpiredIn();
        }
        UserResponse userResponse = mapUserToUserResponse(user);
        userResponse.setToken(jwt);
        userResponse.setTokenExpiresIn(expiresIn);

        return userResponse;
    }

    @Override
    public UserResponse setNewPassword(UUID id, NewPassordRequest request) throws Exception {
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new GeneralException("Passwords don't match", HttpStatus.BAD_REQUEST);
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
            requests.add(u);
        }
        return requests.stream()
                .map(user -> mapUserToUserResponse(user))
                .collect(Collectors.toList());
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }
}
