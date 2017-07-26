package com.eustimenko.portfolio.ws.auth.logic.dto.type;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

public class AuthCredentials implements Serializable {

    private final String email;
    private final String password;

    @JsonCreator
    protected AuthCredentials(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static AuthCredentials of(String email, String password) {
        return new AuthCredentials(email, password);
    }
}