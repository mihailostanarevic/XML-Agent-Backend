package com.rentacar.agentbackend.dto.response;

import lombok.Data;

@Data
public class PhotoResponse {

    private String name;

    private String type;

    private byte[] picByte;

}
