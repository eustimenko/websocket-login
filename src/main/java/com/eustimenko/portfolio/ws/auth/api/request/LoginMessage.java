package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.data.LoginCredentials;
import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoginMessage extends Message {

    private LoginCredentials data;

    public LoginMessage() {
    }

    public LoginMessage(String sequenceId, LoginCredentials data) {
        super(TYPE_OF_MESSAGE.LOGIN_CUSTOMER, sequenceId);
        this.data = data;
    }

    public LoginCredentials getData() {
        return data;
    }

    public void setData(LoginCredentials data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isCorrect() {
        return type == TYPE_OF_MESSAGE.LOGIN_CUSTOMER;
    }
}
