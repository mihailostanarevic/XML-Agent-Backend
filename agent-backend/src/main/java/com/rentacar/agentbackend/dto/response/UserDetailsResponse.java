package com.rentacar.agentbackend.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDetailsResponse {

    private UUID id;

    private String username;

    private String userRole;

    private String name;

    private List<RoleResponse> roleList;

}
