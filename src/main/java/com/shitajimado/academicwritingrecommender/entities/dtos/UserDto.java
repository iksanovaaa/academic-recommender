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

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
