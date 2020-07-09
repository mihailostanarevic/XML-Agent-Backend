package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarGPS extends BaseEntity {

    @Column(unique = true, nullable = false)
    private UUID carId;

    private String lat;

    private String lng;

    private UUID customerId;

    private boolean deleted;
}
