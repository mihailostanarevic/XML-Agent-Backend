package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.entity.SimpleUser;

public interface IEmailService {

    void approveRegistrationMail(SimpleUser simpleUser);
}
