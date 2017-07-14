package com.eustimenko.portfolio.ws.auth.api.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ERROR {

    MESSAGE_IS_NULL("message.null", "Incoming message is null"),
    TYPE_IS_INCORRECT("type.incorrect", "Incoming message type is incorrect"),
    DATA_IS_INCORRECT("data.incorrect", "Incoming message data is incorrect"),
    CUSTOMER_NOT_FOUND("customer.notFound", "Customer not found"),
    PASSWORD_IS_INCORRECT("password.incorrect", "Customer password is incorrect");

    private final String errorCode;
    private final String errorDescription;

    ERROR(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
