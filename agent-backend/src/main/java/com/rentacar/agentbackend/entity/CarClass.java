package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarClass extends BaseEntity {

    private String name;

    private String description;

    @OneToMany(mappedBy = "carClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarModel> carModels = new ArrayList<>();

    private boolean deleted;
}
