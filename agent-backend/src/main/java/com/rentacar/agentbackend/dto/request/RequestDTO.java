package com.rentacar.agentbackend.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class RequestDTO {

    private UUID adID;

    private UUID customerID;

    private String pickUpDate;      // format -> "2016-06-12"

    private String pickUpTime;      // format -> "06:30"
    
    private String returnDate;

    private String returnTime;

    private UUID pickUpAddress;

    private boolean bundle;

}
