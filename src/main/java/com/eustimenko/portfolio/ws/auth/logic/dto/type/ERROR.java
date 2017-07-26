package com.eustimenko.portfolio.ws.auth.logic.dto.type;

import com.fasterxml.jackson.annotation.*;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ERROR {

    MESSAGE_IS_NULL("message.null", "Incoming message is null or has no sequence id"),
    FORMAT_IS_INCORRECT("format.incorrect", "Incoming message format is incorrect"),
    DATA_IS_INCORRECT("data.incorrect", "Incoming message data is incorrect"),
    CUSTOMER_NOT_FOUND("customer.notFound", "Email or password is incorrect");

    @JsonProperty("error_code")
    private final String errorCode;
    @JsonProperty("error_description")
    private final String errorDescription;

    ERROR(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
