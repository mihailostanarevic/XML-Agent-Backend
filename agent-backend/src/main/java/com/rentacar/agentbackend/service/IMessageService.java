package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.SeenRequest;
import com.rentacar.agentbackend.dto.request.SendMessageRequest;
import com.rentacar.agentbackend.dto.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface IMessageService {

    List<MessageResponse> getAllReceivedMessagesForUser(UUID user);
    void sendMessage(SendMessageRequest request);
    List<MessageResponse> getAllSentMessagesFromUser(UUID user);
    void seen(SeenRequest request, UUID id);
    List<MessageResponse> getMessagesBetweenUsers(UUID receiver, UUID sender);
}
