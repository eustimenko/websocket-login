package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.dto.LoginCredentials;
import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

public class LoginMessage extends Message {

    private LoginCredentials data;

    public LoginMessage() {
    }

    public LoginCredentials getData() {
        return data;
    }

    public void setData(LoginCredentials data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isIncorrect() {
        return type != TYPE_OF_MESSAGE.LOGIN_CUSTOMER;
    }

    @JsonIgnore
    public boolean hasNoData(){
        return data == null
        || StringUtils.isEmpty(data.getEmail())
        || StringUtils.isEmpty(data.getPassword());
    }
}
