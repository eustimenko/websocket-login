package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.dto.Token;
import com.fasterxml.jackson.annotation.*;

public class SuccessMessage extends Message<Token> {

    @JsonCreator
    public SuccessMessage(@JsonProperty("sequenceId") String sequenceId, @JsonProperty("data") Token data) {
        super(sequenceId, data);
    }
}
