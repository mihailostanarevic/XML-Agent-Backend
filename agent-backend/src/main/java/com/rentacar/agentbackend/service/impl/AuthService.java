package com.rentacar.agentbackend.service.impl;

import com.github.rkpunjal.sqlsafe.SqlSafeUtil;
import com.rentacar.agentbackend.dto.request.*;
import com.rentacar.agentbackend.dto.response.UserResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.security.TokenUtils;
import com.rentacar.agentbackend.service.IAuthService;
import com.rentacar.agentbackend.service.IEmailService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final IEmailService _emailService;

    @Autowired
    private IAuthorityRepository _authorityRepository;

    public AuthService(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAgentRepository agentRepository, ISimpleUserRepository simpleUserRepository, IAdminRepository adminRepository, AuthenticationManager authenticationManager, TokenUtils tokenUtils, IEmailService emailService) {
        _passwordEncoder = passwordEncoder;
        _userRepository = userRepository;
        _agentRepository = agentRepository;
        _simpleUserRepository = simpleUserRepository;
        _adminRepository = adminRepository;
        _authenticationManager = authenticationManager;
        _tokenUtils = tokenUtils;
        _emailService = emailService;
    }

    /**
     * checkSQLInjection prevent sql injection
     */
    @Override
    public void checkSQLInjection(CreateAgentRequest request)throws GeneralException {
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getUsername())) {
            logger.warn("SQL Injection attempt!");
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        }
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getPassword())){
            logger.warn("SQL Injection attempt!");
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        }
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getRePassword())){
            logger.warn("SQL Injection attempt!");
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        }
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getName())){
            logger.warn("SQL Injection attempt!");
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        }
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getTin())){
            logger.warn("SQL Injection attempt!");
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        }
        if(!SqlSafeUtil.isSqlInjectionSafe(request.getBankAccountNumber())){
            logger.warn("SQL Injection attempt!");
            throw new GeneralException("Nice try!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserResponse createAgent(CreateAgentRequest request) throws Exception {
        if(!request.getPassword().equals(request.getRePassword())){
            logger.info(request.getUsername() + " didn't match his/hers passwords");
            throw new Exception("Passwords don't match.");
        }
        checkSQLInjection(request);
        User user = new User();
        Agent agent = new Agent();
        user.setDeleted(false);
        user.setHasSignedIn(false);
        user.setUsername(request.getUsername());
        user.setPassword(_passwordEncoder.encode(request.getPassword()));
//        user.setUserRole(UserRole.AGENT);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(_authorityRepository.findByName("ROLE_SIMPLE_USER"));
        authorities.add(_authorityRepository.findByName("ROLE_AD_USER"));
        authorities.add(_authorityRepository.findByName("ROLE_MESSAGE_USER"));
        authorities.add(_authorityRepository.findByName("ROLE_AGENT"));
        user.setAuthorities(new HashSet<>(authorities));
        user.setUserRole(UserRole.AGENT);
        agent.setName(request.getName());
        agent.setBankAccountNumber(request.getBankAccountNumber());
        agent.setDateFounded(request.getDateFounded());
        agent.setTin(request.getTin());
//        agent.setRequestStatus(RequestStatus.PENDING);
        Agent savedAgent = _agentRepository.save(agent);
        savedAgent.setUser(user);
        user.setAgent(savedAgent);
        User savedUser = _userRepository.save(user);

        logger.info(user.getUsername() + " account has been successfully created as an agent");
        return mapUserToUserResponse(savedUser);
    }

    @Override
    public UserResponse createSimpleUser(CreateSimpleUserRequest request) throws Exception {
        if(!request.getPassword().equals(request.getRePassword())){
            logger.info(request.getUsername() + " didn't match his passwords");
            throw new Exception("Passwords don't match.");
        }
        User user = new User();
        SimpleUser simpleUser = new SimpleUser();
        user.setDeleted(false);
        user.setHasSignedIn(false);
        user.setUsername(request.getUsername());
        user.setPassword(_passwordEncoder.encode(request.getPassword()));
//        user.setUserRole(UserRole.SIMPLE_USER);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(_authorityRepository.findByName("ROLE_SIMPLE_USER"));
        authorities.add(_authorityRepository.findByName("ROLE_RENT_USER"));
        user.setAuthorities(new HashSet<>(authorities));
        user.setUserRole(UserRole.SIMPLE_USER);
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

        logger.info(user.getUsername() + " account has been successfully created as a simple user");
        return mapUserToUserResponse(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest request) throws Exception {
        User user = _userRepository.findOneByUsername(request.getUsername());
        if(user.getSimpleUser() != null && user.getSimpleUser().getRequestStatus().equals(RequestStatus.PENDING)){
            throw new Exception("Your registration hasn't been approved yet.");
        }

        if(user.getSimpleUser() != null && user.getSimpleUser().getRequestStatus().equals(RequestStatus.DENIED)){
            throw new Exception("Your registration has been denied.");
        }

        if(user.getSimpleUser() != null && user.getSimpleUser().getRequestStatus().equals(RequestStatus.CONFIRMED)){
            throw new Exception("Your registration has been approved by admin. Please activate your account.");
        }
        logger.error("Example of an error which will be displayed because its higher priority than INFO");
        String mail = request.getUsername();
        String password = request.getPassword();
        Authentication authentication = null;
        try {
            authentication = _authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(mail, password));
        }catch (BadCredentialsException e){
            logger.info("entered incorrect credentials!");
            throw new GeneralException("Bad credentials.", HttpStatus.BAD_REQUEST);
        }catch (DisabledException e){
            throw new GeneralException("Your registration request hasn't been approved yet.", HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.warn("An unknown exception happened upon login attempt");
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

        logger.info(user.getUsername() + " has logged in");
        return userResponse;
    }

    @Override
    public UserResponse setNewPassword(UUID id, NewPassordRequest request) throws Exception {
        if (!request.getPassword().equals(request.getRePassword())) {
            logger.info("User didn't match his passwords when trying to change password");
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
        logger.info(user.getUsername() + " has changed his password");

        if(!user.isHasSignedIn()){
            user.setHasSignedIn(true);
        }

        if(admin != null){
            _adminRepository.save(admin);
        }

        return mapUserToUserResponse(user);
    }

    @Override
    public void confirmRegistrationRequest(GetIdRequest request) throws Exception {
        SimpleUser simpleUser = _simpleUserRepository.findOneById(request.getId());
        simpleUser.setRequestStatus(RequestStatus.CONFIRMED);
        LocalDateTime currentTime = LocalDateTime.now();
        simpleUser.setConfirmationTime(currentTime);
        _simpleUserRepository.save(simpleUser);

        _emailService.approveRegistrationMail(simpleUser);
    }

    @Override
    public void approveRegistrationRequest(GetIdRequest request) throws Exception {
        SimpleUser simpleUser = _simpleUserRepository.findOneById(request.getId());
        if(simpleUser.getConfirmationTime().plusHours(12L).isBefore(LocalDateTime.now())){
            simpleUser.setRequestStatus(RequestStatus.DENIED);
            _simpleUserRepository.save(simpleUser);
            throw new Exception("Your activation link has expired.");
        }
        simpleUser.setRequestStatus(RequestStatus.APPROVED);
        _simpleUserRepository.save(simpleUser);
    }

    @Override
    public void denyRegistrationRequest(GetIdRequest request) throws Exception {
        SimpleUser simpleUser = _simpleUserRepository.findOneById(request.getId());
        simpleUser.setRequestStatus(RequestStatus.DENIED);
        _simpleUserRepository.save(simpleUser);
    }

    @Override
    public List<UserResponse> getAllRegistrationRequests() throws Exception {
        List<User> users = _userRepository.findAllByDeleted(false);
        List<User> requests = new ArrayList<>();
        for (User u: users){
            if(u.getRoles().contains(_authorityRepository.findByName("ROLE_SIMPLE_USER"))){
                SimpleUser simpleUser = _simpleUserRepository.findOneByUser(u);
                if(simpleUser != null && simpleUser.getRequestStatus().equals(RequestStatus.PENDING)){
                    requests.add(u);
                }
            }
        }
        return requests.stream()
                .map(user -> mapUserToUserResponse(user))
                .collect(Collectors.toList());
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        if(user.getSimpleUser() != null){
            userResponse.setId(user.getSimpleUser().getId());
        }else if(user.getAgent() != null){
            userResponse.setId(user.getAgent().getId());
        }else if(user.getAdmin() != null){
            userResponse.setId(user.getAdmin().getId());
        }
        userResponse.setUsername(user.getUsername());
        if(user.getUserRole().equals(UserRole.ADMIN)){
            userResponse.setUserRole("ADMIN_ROLE");
        }else if(user.getUserRole().equals(UserRole.AGENT)){
            userResponse.setUserRole("AGENT_ROLE");
        }else if(user.getUserRole().equals(UserRole.SIMPLE_USER)){
            userResponse.setUserRole("SIMPLE_USER_ROLE");
        }
        return userResponse;
    }
}
