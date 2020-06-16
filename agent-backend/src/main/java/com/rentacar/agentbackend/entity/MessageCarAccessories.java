package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class MessageCarAccessories extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "car_accessories_id")
    private CarAccessories car_accessory;

    private boolean approved;

    private boolean reviewed;

    public MessageCarAccessories(){
        this.approved = false;
        this.reviewed = false;
    }
}
