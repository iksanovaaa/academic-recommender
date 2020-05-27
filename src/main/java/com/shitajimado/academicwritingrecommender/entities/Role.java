package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    @Id
    String id;
    String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}