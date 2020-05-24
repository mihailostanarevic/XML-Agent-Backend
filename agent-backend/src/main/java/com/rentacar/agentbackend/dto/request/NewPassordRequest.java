package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class NewPassordRequest {

    private String password;

    private String rePassword;
}
