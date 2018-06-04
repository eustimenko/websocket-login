package com.eustimenko.services.auth.message.data;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public enum ErrorType {
    CUSTOMER_NOT_FOUND("customer.notFound", "Customer not found"),
    INCORRECT_REQUEST_FORMAT("request.incorrect", "Request format is incorrect");

    @NotEmpty
    private final String code;
    @NotEmpty
    private final String description;

    @JsonCreator
    ErrorType(@JsonProperty("error_code") String code, @JsonProperty("error_description") String description) {
        this.code = code;
        this.description = description;
    }
}
