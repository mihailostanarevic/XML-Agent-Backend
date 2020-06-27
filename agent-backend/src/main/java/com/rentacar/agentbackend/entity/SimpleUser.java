package com.rentacar.agentbackend.entity;

import com.rentacar.agentbackend.util.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUser extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String firstName;

    private String lastName;

    private String ssn; //jmbg

    private String address;

    private String city;

    private String country;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //
    private Set<Request> request;

    private LocalDateTime confirmationTime;

    @OneToOne(mappedBy = "simpleUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SecurityQuestions securityQuestions;

    @OneToMany(mappedBy = "simpleUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //
    private List<Rating> ratings;

    @OneToMany(mappedBy = "simpleUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //
    private List<Comment> comments;
}
