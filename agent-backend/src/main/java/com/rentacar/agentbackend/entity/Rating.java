package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating extends BaseEntity {

    private String grade; //rating

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simple_user_id")
    private SimpleUser simpleUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;
}
