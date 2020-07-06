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
public class PriceList extends BaseEntity {

    private String price1day;

    private String discount7days;

    private String discount15days;

    private String discount30days;

    @Column(unique = true, nullable = false)
    private UUID agentId;

    private boolean deleted;
}
