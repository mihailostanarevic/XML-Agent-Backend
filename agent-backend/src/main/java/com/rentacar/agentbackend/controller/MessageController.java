package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.SeenRequest;
import com.rentacar.agentbackend.dto.request.SendMessageRequest;
import com.rentacar.agentbackend.dto.response.MessageResponse;
import com.rentacar.agentbackend.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final IMessageService _messageService;

    public MessageController(IMessageService messageService) {
        _messageService = messageService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SEND_MESSAGE')")
    public void sendMessage(@RequestBody SendMessageRequest request){
        _messageService.sendMessage(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RECEIVE_MESSAGE')")
    public List<MessageResponse> getMessages(@RequestParam("receiver") UUID receiverID, @RequestParam("sender") UUID senderID){
        if(senderID == null)
            return _messageService.getAllReceivedMessagesForUser(receiverID);
        else
            return _messageService.getMessagesBetweenUsers(receiverID, senderID);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SEND_MESSAGE')")
    public void seen(@RequestBody SeenRequest request, @PathVariable("id") UUID id){
        _messageService.seen(request, id);
    }
}
