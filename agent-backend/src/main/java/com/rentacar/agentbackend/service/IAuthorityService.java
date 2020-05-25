package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.entity.Authority;

import java.util.List;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public interface IAuthorityService {

    List<Authority> findByname(String name);

}
