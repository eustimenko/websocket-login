package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.dto.ERROR;
import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;

public class ErrorMessage extends Message {

    private ERROR data;

    public ErrorMessage() {
    }

    private ErrorMessage(ERROR data, String sequenceId) {
        super(TYPE_OF_MESSAGE.CUSTOMER_ERROR, sequenceId);
        this.data = data;
    }

    private ErrorMessage(ERROR data) {
        this(data, null);
    }

    public ERROR getData() {
        return data;
    }

    public void setData(ERROR data) {
        this.data = data;
    }



    public static ErrorMessage typeError(String s) {
        return new ErrorMessage(ERROR.TYPE_IS_INCORRECT, s);
    }

    public static ErrorMessage nullMessageError() {
        return new ErrorMessage(ERROR.MESSAGE_IS_NULL);
    }

    public static ErrorMessage dataError(String s) {
        return new ErrorMessage(ERROR.DATA_IS_INCORRECT, s);
    }

    public static ErrorMessage customerNotFoundError(String s) {
        return new ErrorMessage(ERROR.CUSTOMER_NOT_FOUND, s);
    }

    public static ErrorMessage passwordError(String s) {
        return new ErrorMessage(ERROR.PASSWORD_IS_INCORRECT, s);
    }
}
