package com.rentacar.agentbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@SuppressWarnings("unused")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authority implements GrantedAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name") String name;     // ADMIN, USER...

    @ManyToMany(mappedBy = "authorities")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
        name = "authorities_permissions",
        joinColumns = @JoinColumn(
                name = "authority_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
                name = "permission_id", referencedColumnName = "id"))
    private Collection<Permission> permissions;

    @Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


