package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserResponse {

    private UUID id;

    private String username; //email

    private String token;

    private int tokenExpiresIn;
}
