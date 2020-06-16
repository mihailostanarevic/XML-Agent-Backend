package com.rentacar.agentbackend.entity;

import com.rentacar.agentbackend.util.enums.CarRequestStatus;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Request extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "simple_user_id") //
    private SimpleUser customer;

    private RequestStatus status;

    private LocalDate receptionDate;       // datum prijema zahteva

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address pickUpAddress;

    private boolean deleted;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestAd> requestAds = new HashSet<RequestAd>();

    public Request() {
        this.receptionDate = LocalDate.now();
    }

}
