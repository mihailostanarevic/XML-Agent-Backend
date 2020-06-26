package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity {

    private String street;

    private int number;

    private String city;

    private String country;

    @OneToMany(mappedBy = "pickUpAddress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Request> request = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Agent> agent = new HashSet<>();

}
