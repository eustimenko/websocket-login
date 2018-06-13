package com.eustimenko.services.auth.message;

import com.eustimenko.services.auth.message.data.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class ErrorMessage extends Message {

    @Valid
    @NotNull
    private final ErrorType data;

    @JsonCreator
    public ErrorMessage(@JsonProperty("sequence_id") String sequenceId,
                        @JsonProperty("data") ErrorType data
    ) {
        super(MessageType.CUSTOMER_ERROR, sequenceId);
        this.data = data;
    }

    public ErrorMessage(@JsonProperty("data") ErrorType data) {
        this(null, data);
    }
}
