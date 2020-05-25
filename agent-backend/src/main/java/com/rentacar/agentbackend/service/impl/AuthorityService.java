package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.entity.Authority;
import com.rentacar.agentbackend.repository.IAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
@Service
public class AuthorityService {

    @Autowired
    private IAuthorityRepository _authorityRepository;

    public List<Authority> findByname(String name) {
        Authority auth = this._authorityRepository.findByName(name);
        ArrayList<Authority> auths = new ArrayList<>();
        auths.add(auth);
        return auths;
    }

}
