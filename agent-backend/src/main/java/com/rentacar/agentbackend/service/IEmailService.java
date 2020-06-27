package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.entity.Agent;
import com.rentacar.agentbackend.entity.SimpleUser;

public interface IEmailService {

    void approveRegistrationMail(SimpleUser simpleUser);

    void agentRegistrationMail(Agent agent);

    void newPasswordAnnouncementMail(SimpleUser simpleUser, String pass);
}
