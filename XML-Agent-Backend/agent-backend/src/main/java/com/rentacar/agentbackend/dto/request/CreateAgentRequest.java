package com.rentacar.agentbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CreateAgentRequest {

    private String username;

    private String password;

    private String rePassword;

    private String name;

    private String tin;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date dateFounded;

    private String bankAccountNumber;
}
