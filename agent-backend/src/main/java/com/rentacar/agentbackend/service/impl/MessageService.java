package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.SeenRequest;
import com.rentacar.agentbackend.dto.request.SendMessageRequest;
import com.rentacar.agentbackend.dto.response.AdMessageResponse;
import com.rentacar.agentbackend.dto.response.CarAccessoryResponse;
import com.rentacar.agentbackend.dto.response.MessageResponse;
import com.rentacar.agentbackend.dto.response.UserMessageResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IMessageService;
import com.rentacar.agentbackend.service.IUserService;
import com.rentacar.agentbackend.util.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private IUserService _userService;

    @Autowired
    private IMessageCarAccessoriesRepository _messageCarAccessoriesRepository;

    @Autowired
    private IMessageRepository _messageRepository;

    @Autowired
    private IAdRepository _adRepository;

    @Autowired
    private ICarAccessoriesRepository _carAccessoriesRepository;

    @Autowired
    private ISimpleUserRepository _simpleUserRepository;

    @Autowired
    private IAgentRepository _agentRepository;

    @Override
    public List<MessageResponse> getAllReceivedMessagesForUser(UUID id) {
        SimpleUser simpleUser = _simpleUserRepository.findOneById(id);
        User user;
        if(simpleUser != null){
            user = _userService.getUser(simpleUser.getUser().getId());
        }else {
            Agent agent = _agentRepository.findOneById(id);
            user = _userService.getUser(agent.getUser().getId());
        }
        return mapMessagesToResponseDTO(user);
    }

    @Override
    public void sendMessage(SendMessageRequest request) {

        Message newMessage = new Message();
        newMessage.setText(request.getText());

        Ad ad = _adRepository.getOne(request.getAd());
        newMessage.setAd(ad);

        SimpleUser simpleUser = _simpleUserRepository.findOneById(request.getSender());
        if(simpleUser != null){
            newMessage.setUserSender(simpleUser.getUser());
        }else {
            Agent agent = _agentRepository.findOneById(request.getSender());
            newMessage.setUserSender(agent.getUser());
        }

        SimpleUser simpleUser1 = _simpleUserRepository.findOneById(request.getReceiver());
        if(simpleUser1 != null){
            newMessage.setUserReceiver(simpleUser1.getUser());
        }else {
            Agent agent = _agentRepository.findOneById(request.getReceiver());
            newMessage.setUserReceiver(agent.getUser());
        }

        Message msg = _messageRepository.save(newMessage);
        for(UUID id : request.getAccessories()){
            MessageCarAccessories mca = new MessageCarAccessories();
            mca.setMessage(msg);
            CarAccessories carAccess = _carAccessoriesRepository.findOneById(id);
            mca.setCar_accessory(carAccess);
            _messageCarAccessoriesRepository.save(mca);
        }
//        return new ResponseEntity<String>("Message sent", HttpStatus.CREATED);
    }

    @Override
    public List<MessageResponse> getAllSentMessagesFromUser(UUID user) {
        return null;
    }

    @Override
    public void seen(SeenRequest request, UUID id) {
        Message msg = _messageRepository.findOneById(id);
        msg.setSeen(request.isSeen());
        _messageRepository.save(msg);
    }

    @Override
    public List<MessageResponse> getMessagesBetweenUsers(UUID receiver, UUID sender) {
        SimpleUser simpleUserReceiver = _simpleUserRepository.findOneById(receiver);
        User userReceiver;
        if(simpleUserReceiver != null){
            userReceiver = _userService.getUser(simpleUserReceiver.getUser().getId());
        }else {
            Agent agentReceiver = _agentRepository.findOneById(receiver);
            userReceiver = _userService.getUser(agentReceiver.getUser().getId());
        }

        SimpleUser simpleUserSender = _simpleUserRepository.findOneById(sender);
        User userSender;
        if(simpleUserSender != null){
            userSender = _userService.getUser(simpleUserSender.getUser().getId());
        }else {
            Agent agentSender = _agentRepository.findOneById(sender);
            userSender = _userService.getUser(agentSender.getUser().getId());
        }

        List<Message> allMessages = _messageRepository.findAll();
        allMessages = allMessages.stream().filter(message -> ((message.getUserSender().getId().equals(userSender.getId()) && message.getUserReceiver().getId().equals(userReceiver.getId())) || (message.getUserReceiver().getId().equals(userSender.getId()) && message.getUserSender().getId().equals(userReceiver.getId()))))
                .collect(Collectors.toList());

        return mapMessagesBetweenTwoUsersToDTO(allMessages);
    }

    private List<MessageResponse> mapMessagesBetweenTwoUsersToDTO(List<Message> allMessages) {
        List<MessageResponse> retVal = new ArrayList<>();
        for(Message message : allMessages){
            MessageResponse dto = new MessageResponse();
            dto.setId(message.getId());
            dto.setText(message.getText());
            dto.setDateSent(message.getDateSent().toString());
            dto.setTimeSent(message.getTimeSent().toString());

            AdMessageResponse adDTO = new AdMessageResponse();
            adDTO.setShortCarDescription(makeShortCarDescription(message));
            adDTO.setAdID(message.getAd().getId());
            adDTO.setDateIssued(message.getAd().getCreationDate().toString());
            dto.setAd(adDTO);

            UserMessageResponse userDTO = new UserMessageResponse();
            if(message.getUserSender().getUserRole().equals(UserRole.AGENT)){
                userDTO.setId(message.getUserSender().getAgent().getId());
                userDTO.setName(message.getUserSender().getAgent().getName());
            }else if(message.getUserSender().getUserRole().equals(UserRole.ADMIN)){
                userDTO.setId(message.getUserSender().getAdmin().getId());
                userDTO.setName(message.getUserSender().getAdmin().getFirstName() + " " + message.getUserSender().getAdmin().getLastName());
            }else {
                userDTO.setId(message.getUserSender().getSimpleUser().getId());
                userDTO.setName(message.getUserSender().getSimpleUser().getFirstName() + " " + message.getUserSender().getSimpleUser().getLastName());
            }
            dto.setUser(userDTO);

            List<MessageCarAccessories> list = _messageCarAccessoriesRepository.findAll();
            List<CarAccessoryResponse> accessories = new ArrayList<>();
            for(MessageCarAccessories item : list){
                if(item.getMessage().getId().equals(message.getId())){
                    if(!item.isReviewed()){
                        CarAccessoryResponse carAccessoryResponse = new CarAccessoryResponse(
                                item.getCar_accessory().getId(),
                                item.getId(),
                                item.getCar_accessory().getDescription());
                        accessories.add(carAccessoryResponse);
                    }
                }
            }
            dto.setCarAccessories(accessories);
            dto.setSeen(message.isSeen());
            retVal.add(dto);
        }

        return retVal;
    }

    private List<MessageResponse> mapMessagesToResponseDTO(User user) {
        List<MessageResponse> retVal = new ArrayList<>();

        for(Message message : user.getMessagesReceived()){
            MessageResponse msg = new MessageResponse();
            msg.setId(message.getId());
            msg.setText(message.getText());
            msg.setDateSent(message.getDateSent().toString());
            msg.setTimeSent(message.getTimeSent().toString());
            AdMessageResponse ad = new AdMessageResponse(message.getAd().getCreationDate().toString()
                    , makeShortCarDescription(message)
                    , message.getAd().getId());
            msg.setAd(ad);
            UserMessageResponse userDTO = new UserMessageResponse();


            if(message.getUserSender().getUserRole().equals(UserRole.AGENT)){
                userDTO.setId(message.getUserSender().getAgent().getId());
                userDTO.setName(message.getUserSender().getAgent().getName());
            }else if(message.getUserSender().getUserRole().equals(UserRole.ADMIN)){
                userDTO.setId(message.getUserSender().getAdmin().getId());
                userDTO.setName(message.getUserSender().getAdmin().getFirstName() + " " + message.getUserSender().getAdmin().getLastName());
            }else {
                userDTO.setId(message.getUserSender().getSimpleUser().getId());
                userDTO.setName(message.getUserSender().getSimpleUser().getFirstName() + " " + message.getUserSender().getSimpleUser().getLastName());
            }
            msg.setUser(userDTO);

            List<MessageCarAccessories> list = _messageCarAccessoriesRepository.findAll();
            List<CarAccessoryResponse> accessories = new ArrayList<>();
            for(MessageCarAccessories item : list){
                if(item.getMessage().getId().equals(message.getId())){
                    if(!item.isReviewed()){
                        CarAccessoryResponse carAccessoryResponse = new CarAccessoryResponse(
                                item.getCar_accessory().getId(),
                                item.getId(),
                                item.getCar_accessory().getDescription());
                        accessories.add(carAccessoryResponse);
                    }
                }
            }
            msg.setCarAccessories(accessories);
            msg.setSeen(message.isSeen());
            retVal.add(msg);
        }

        return retVal;
    }

    public String makeShortCarDescription(Message message){
        String retVal = "";

        retVal += message.getAd().getCar().getCarModel().getCarBrand().getName();
        retVal += " ";
        retVal += message.getAd().getCar().getCarModel().getName();
        retVal += ", ";
        retVal += message.getAd().getCar().getCarModel().getCarClass().getName();

        return retVal;
    }
}
