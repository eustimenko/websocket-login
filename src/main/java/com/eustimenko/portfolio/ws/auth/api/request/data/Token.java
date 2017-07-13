package com.eustimenko.portfolio.ws.auth.api.request.data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Token implements Serializable {

    private String apiToken;
    private LocalDateTime apiTokenExpirationDate;

    public Token(com.eustimenko.portfolio.ws.auth.persistent.entity.Token entity) {
        this.apiToken = entity.getToken();
        this.apiTokenExpirationDate = entity.getExpiredDate();
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
}
