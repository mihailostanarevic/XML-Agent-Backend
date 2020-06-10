package com.rentacar.agentbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Photo extends BaseEntity {

    private String name;

    private String type;

    @Column(name = "pic_byte", length = 1000)
    private byte[] picByte;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Ad ad;

    public Photo() {
        this.deleted = false;
    }

    public Photo(String name, String type, byte[] picByte, boolean deleted, Ad ad) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
        this.deleted = deleted;
        this.ad = ad;
    }
}
