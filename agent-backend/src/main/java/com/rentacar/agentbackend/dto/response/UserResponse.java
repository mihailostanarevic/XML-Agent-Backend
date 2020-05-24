package com.rentacar.agentbackend.dto.response;

import com.rentacar.agentbackend.util.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID id;

    private String username; //email

    private UserRole userRole;

    private boolean hasSignedIn;
}
