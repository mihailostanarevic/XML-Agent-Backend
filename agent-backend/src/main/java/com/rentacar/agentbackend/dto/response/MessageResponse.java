package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private UUID id;

    private String text;

    private String dateSent;

    private String timeSent;

    private AdMessageResponse ad;

    private UserMessageResponse user;

    private List<CarAccessoryResponse> carAccessories;

    private boolean seen;
}
