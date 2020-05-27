package com.shitajimado.academicwritingrecommender.entities;

import com.shitajimado.academicwritingrecommender.core.PasswordConverter;
import com.shitajimado.academicwritingrecommender.entities.dtos.UserDto;
import org.springframework.data.annotation.Id;

public class User {
    @Id
    private String id;
    private String login;
    private String password;

    public User() {

    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(UserDto userDto) {
        this(userDto.getLogin(), PasswordConverter.convert(userDto.getPassword()));
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String nonEncodedPassword) {
        var encoded = PasswordConverter.convert(nonEncodedPassword);
        return this.password.equals(encoded);
    }
}
