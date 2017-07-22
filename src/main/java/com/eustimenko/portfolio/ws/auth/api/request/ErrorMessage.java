package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.dto.ERROR;
import com.fasterxml.jackson.annotation.*;

public class ErrorMessage extends Message<ERROR> {

    @JsonCreator
    public ErrorMessage(@JsonProperty("sequenceId") String sequenceId, @JsonProperty("data") ERROR data) {
        super(sequenceId, data);
    }

    private ErrorMessage(ERROR data) {
        this(null, data);
    }

    public static ErrorMessage typeError() {
        return new ErrorMessage(ERROR.TYPE_IS_INCORRECT);
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

    public static ErrorMessage passwordError(String s) {
        return new ErrorMessage(s, ERROR.PASSWORD_IS_INCORRECT);
    }
}
