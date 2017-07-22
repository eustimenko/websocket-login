package com.eustimenko.portfolio.ws.auth.api.dto;

import com.eustimenko.portfolio.ws.auth.api.dto.type.Token;
import com.fasterxml.jackson.annotation.*;

public class SuccessMessage extends Message<Token> {

    @JsonCreator
    public SuccessMessage(@JsonProperty("sequenceId") String sequenceId, @JsonProperty("data") Token data) {
        super(sequenceId, data);
    }
}
