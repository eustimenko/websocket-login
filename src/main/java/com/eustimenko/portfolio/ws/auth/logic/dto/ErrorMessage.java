package com.eustimenko.portfolio.ws.auth.logic.dto;

import com.eustimenko.portfolio.ws.auth.logic.dto.type.ERROR;
import com.fasterxml.jackson.annotation.*;

public class ErrorMessage extends Message<ERROR> {

    @JsonCreator
    protected ErrorMessage(@JsonProperty("sequence_id") String sequenceId, @JsonProperty("data") ERROR data) {
        super(sequenceId, data);
    }

    private ErrorMessage(ERROR data) {
        this(null, data);
    }

    public static ErrorMessage typeError() {
        return new ErrorMessage(ERROR.FORMAT_IS_INCORRECT);
    }

    public static ErrorMessage nullMessageError() {
        return new ErrorMessage(ERROR.MESSAGE_IS_NULL);
    }

    public static ErrorMessage dataError(String s) {
        return new ErrorMessage(s, ERROR.DATA_IS_INCORRECT);
    }

    public static ErrorMessage customerNotFoundError(String s) {
        return new ErrorMessage(s, ERROR.CUSTOMER_NOT_FOUND);
    }

    public String toString() {
        return sequenceId + ":" + data;
    }
}
