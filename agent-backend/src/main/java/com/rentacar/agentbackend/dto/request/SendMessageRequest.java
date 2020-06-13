package com.rentacar.agentbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    private String text;

    private UUID sender;

    private UUID receiver;

    private UUID ad;

    private List<UUID> accessories;
}
