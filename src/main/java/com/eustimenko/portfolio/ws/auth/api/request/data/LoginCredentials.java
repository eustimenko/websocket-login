package com.eustimenko.portfolio.ws.auth.api.request.data;

import java.io.Serializable;

public class LoginCredentials implements Serializable {

    private String email;
    private String password;

    public LoginCredentials() {
    }

    public LoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
