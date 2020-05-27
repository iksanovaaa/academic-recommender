package com.shitajimado.academicwritingrecommender.entities;

import com.shitajimado.academicwritingrecommender.entities.dtos.UserDto;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private List<Role> roles;

    public User() {

    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.roles = new ArrayList<>();
        this.roles.add(role);
    }

    public User(UserDto userDto, Role role) {
        this(userDto.getUsername(), userDto.getPassword(), role);
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /* public boolean checkPassword(String nonEncodedPassword) {
        var encoded = PasswordConverter.convert(nonEncodedPassword);
        return this.password.equals(encoded);
    } */

    public List<Role> getRoles() {
        return this.roles;
    }
}
