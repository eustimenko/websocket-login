package com.eustimenko.portfolio.ws.auth.api.request.dto;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

public class LoginCredentials implements Serializable {

    private final String email;
    private final String password;

    @JsonCreator
    public LoginCredentials(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}