package com.eustimenko.portfolio.ws.auth.api.dto;

import com.eustimenko.portfolio.ws.auth.api.dto.type.LoginCredentials;
import com.fasterxml.jackson.annotation.*;
import org.springframework.util.StringUtils;

public class LoginMessage extends Message<LoginCredentials> {

    @JsonCreator
    @JsonPropertyOrder({"type", "sequenceId", "data"})
    public LoginMessage(@JsonProperty("sequenceId") String sequenceId, @JsonProperty("data") LoginCredentials data) {
        super(sequenceId, data);
    }

    @JsonIgnore
    public boolean hasNoData() {
        return data == null
                || StringUtils.isEmpty(data.getEmail())
                || StringUtils.isEmpty(data.getPassword());
    }
}
