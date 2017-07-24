package com.eustimenko.portfolio.ws.auth.api.dto;

import com.eustimenko.portfolio.ws.auth.api.dto.type.Token;
import com.fasterxml.jackson.annotation.*;

public class SuccessMessage extends Message<Token> {

    @JsonCreator
    protected SuccessMessage(@JsonProperty("sequenceId") String sequenceId, @JsonProperty("data") Token data) {
        super(sequenceId, data);
    }

    public static SuccessMessage of(String s, Token data) {
        return new SuccessMessage(s, data);
    }
}
