package com.eustimenko.portfolio.ws.auth.logic.dto;

import com.eustimenko.portfolio.ws.auth.logic.dto.type.Token;
import com.fasterxml.jackson.annotation.*;

public class SuccessMessage extends Message<Token> {

    @JsonCreator
    protected SuccessMessage(@JsonProperty("sequence_id") String sequenceId, @JsonProperty("data") Token data) {
        super(sequenceId, data);
    }

    public static SuccessMessage of(String s, Token data) {
        return new SuccessMessage(s, data);
    }

    public String toString() {
        return sequenceId + ":" + data;
    }
}
