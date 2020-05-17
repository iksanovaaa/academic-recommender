package com.shitajimado.academicwritingrecommender.entities.dtos;

public class UserDto {
    private String login;
    private String password;
    private String matchingPassword;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }
}
