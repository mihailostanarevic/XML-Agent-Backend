package com.rentacar.agentbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarAccessories extends BaseEntity {

    private String description;

    @ManyToMany(mappedBy = "carAccessories")
    private List<Car> cars = new ArrayList<>();

    @OneToMany(mappedBy = "car_accessory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageCarAccessories> messageCarAccessories = new HashSet<MessageCarAccessories>();

    private boolean deleted;
}

