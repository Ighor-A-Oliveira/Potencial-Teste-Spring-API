package com.ighor.jwt_demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name; // EX: ROLE_ADMIN, ROLE_USER

    public Role(String name){
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
