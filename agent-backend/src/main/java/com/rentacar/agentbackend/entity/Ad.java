package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ad extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

//    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Photo> photos = new ArrayList<>();

    private boolean available; //is rented or available

    private boolean limitedDistance; //is distance which user can travel limited

    private String availableKilometersPerRent; //if distance is limited

    private String seats; //child seats

    private boolean cdw;

    private Date date; //date when ad was created

    private boolean deleted;
}
