package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Photo extends BaseEntity {

    private String url;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Ad ad;

    public Photo() {
        this.deleted = false;
    }
}
