package com.eustimenko.portfolio.ws.auth.api.dto;

import com.eustimenko.portfolio.ws.auth.api.dto.type.AuthCredentials;
import com.fasterxml.jackson.annotation.*;
import org.springframework.util.StringUtils;

public class LoginMessage extends Message<AuthCredentials> {

    @JsonCreator
    @JsonPropertyOrder({"type", "sequenceId", "data"})
    public LoginMessage(@JsonProperty("sequenceId") String sequenceId, @JsonProperty("data") AuthCredentials data) {
        super(sequenceId, data);
    }

    @JsonIgnore
    public boolean hasNoData() {
        return data == null
                || StringUtils.isEmpty(data.getEmail())
                || StringUtils.isEmpty(data.getPassword());
    }
}
