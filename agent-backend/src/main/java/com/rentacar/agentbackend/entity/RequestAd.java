package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAd extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    private LocalDate pickUpDate;          // datum preuzimanja

    private LocalTime pickUpTime;           // vreme preuzimanja

    private LocalDate returnDate;           // datum vracanja

    private LocalTime returnTime;           // vreme vracanja

}