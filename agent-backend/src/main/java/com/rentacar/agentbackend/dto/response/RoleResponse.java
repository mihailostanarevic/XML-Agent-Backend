package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleResponse {

    private Long id;

    private String name;

    private List<String> permissionList;

}
