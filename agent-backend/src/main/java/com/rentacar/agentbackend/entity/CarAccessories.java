package com.rentacar.agentbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarAccessories extends BaseEntity {

    private String description;

    @ManyToMany(mappedBy = "carAccessories")
    private List<Car> cars = new ArrayList<>();

    private boolean deleted;
}

