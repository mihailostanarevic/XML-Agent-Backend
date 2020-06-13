package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.SendMessageRequest;
import com.rentacar.agentbackend.dto.response.AdMessageResponse;
import com.rentacar.agentbackend.dto.response.CarAccessoryResponse;
import com.rentacar.agentbackend.dto.response.MessageResponse;
import com.rentacar.agentbackend.dto.response.UserMessageResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.IAdRepository;
import com.rentacar.agentbackend.repository.ICarAccessoriesRepository;
import com.rentacar.agentbackend.repository.IMessageCarAccessoriesRepository;
import com.rentacar.agentbackend.repository.IMessageRepository;
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
import java.util.regex.Pattern;

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

    @Override
    public List<MessageResponse> getAllReceivedMessagesForUser(UUID id) {
        User user = _userService.getUser(id);
        return mapMessagesToResponseDTO(user);
    }

    @Override
    public ResponseEntity<String> sendMessage(SendMessageRequest request) {

        //Jebiga moze nas neko sjebati sa XSS-om
        Pattern pattern = Pattern.compile("<.+?>");
        if(pattern.matcher(request.getText()).matches()){
            return new ResponseEntity<>("Possible XSS attack", HttpStatus.BAD_REQUEST);
        }
        //valja se proveriti da nas teodora ne bi smarala

        Message newMessage = new Message();
        newMessage.setText(request.getText());

        Ad ad = _adRepository.getOne(request.getAd());
        newMessage.setAd(ad);

        User userSender = _userService.getUser(request.getSender());
        User userReceiver = _userService.getUser(request.getReceiver());
        newMessage.setUserSender(userSender);
        newMessage.setUserReceiver(userReceiver);

        Message msg = _messageRepository.save(newMessage);
        for(UUID id : request.getAccessories()){
            MessageCarAccessories mca = new MessageCarAccessories();
            mca.setMessage(msg);
            CarAccessories carAccess = _carAccessoriesRepository.findOneById(id);
            mca.setCar_accessory(carAccess);
            _messageCarAccessoriesRepository.save(mca);
        }
        return new ResponseEntity<>("Message sent", HttpStatus.CREATED);
    }

    @Override
    public List<MessageResponse> getAllSentMessagesFromUser(UUID user) {
        return null;
    }

    private List<MessageResponse> mapMessagesToResponseDTO(User user) {
        List<MessageResponse> retVal = new ArrayList<>();

        for(Message message : user.getMessagesReceived()){
            MessageResponse msg = new MessageResponse();
            msg.setText(message.getText());
            msg.setDateSent(message.getDateSent().toString());
            msg.setTimeSent(message.getText());
            AdMessageResponse ad = new AdMessageResponse(message.getAd().getCreationDate().toString()
                    , makeShortCarDescription(message)
                    , message.getAd().getId());
            msg.setAd(ad);
            UserMessageResponse userDTO = new UserMessageResponse();
            userDTO.setId(user.getId());

            if(user.getUserRole().equals(UserRole.AGENT)){
                userDTO.setName(user.getAgent().getName());
            }else if(user.getUserRole().equals(UserRole.ADMIN)){
                userDTO.setName(user.getAdmin().getFirstName() + " " + user.getAdmin().getLastName());
            }else {
                userDTO.setName(user.getSimpleUser().getFirstName() + " " + user.getAdmin().getLastName());
            }
            msg.setUser(userDTO);

            List<MessageCarAccessories> list = _messageCarAccessoriesRepository.findAll();
            List<CarAccessoryResponse> accessories = new ArrayList<>();
            for(MessageCarAccessories item : list){
                if(item.getMessage().getId().equals(message.getId())){
                    CarAccessoryResponse carAccessoryResponse = new CarAccessoryResponse(
                            item.getCar_accessory().getId()
                            ,item.getCar_accessory().getDescription());
                    accessories.add(carAccessoryResponse);
                }
            }
            msg.setCarAccessories(accessories);
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
