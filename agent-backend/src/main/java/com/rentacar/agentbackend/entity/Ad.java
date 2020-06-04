package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ad extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

//    @ManyToMany(fetch = FetchType.LAZY)
//    private Set<Request> request;

    @OneToMany(mappedBy = "ad")
    private Set<RequestAd> adRequests = new HashSet<RequestAd>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private Agent agent;

//    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Photo> photos = new ArrayList<>();

    private boolean available; //is rented or available

    private boolean limitedDistance; //is distance which user can travel limited

    private String availableKilometersPerRent; //if distance is limited

    private int seats; //child seats

    private boolean cdw;

    private LocalDate date; //date when ad was created

    private boolean deleted;
}
