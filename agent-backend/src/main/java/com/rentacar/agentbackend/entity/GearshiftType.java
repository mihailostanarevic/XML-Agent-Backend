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
public class GearshiftType extends BaseEntity {

    private String type; //ili enum {automatic, manual, semiautomatic}

    private String numberOfGears; //ili enum {four, five, six , seven, eight, nine, ten} poslednjih nekoliko se odnose na automatike

    @OneToMany(mappedBy = "gearshiftType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();

    private boolean deleted;
}
