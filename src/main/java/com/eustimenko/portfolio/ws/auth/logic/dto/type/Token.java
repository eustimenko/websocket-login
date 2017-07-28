package com.eustimenko.portfolio.ws.auth.logic.dto.type;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Token implements Serializable {

    @JsonProperty("api_token")
    private String apiToken;
    @JsonProperty("api_token_expiration_date")
    private LocalDateTime apiTokenExpirationDate;

    @JsonCreator
    protected Token(String apiToken, LocalDateTime apiTokenExpirationDate) {
        this.apiToken = apiToken;
        this.apiTokenExpirationDate = apiTokenExpirationDate;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public LocalDateTime getApiTokenExpirationDate() {
        return apiTokenExpirationDate;
    }

    public void setApiTokenExpirationDate(LocalDateTime apiTokenExpirationDate) {
        this.apiTokenExpirationDate = apiTokenExpirationDate;
    }

    public static Token of(com.eustimenko.portfolio.ws.auth.persistent.entity.Token t) {
        return new Token(t.getValue(), t.getExpiredDate());
    }

    public String toString() {
        return apiToken + ":" + apiTokenExpirationDate;
    }
}
