package com.eustimenko.services.auth.message;

import com.eustimenko.services.auth.entity.Token;
import com.eustimenko.services.auth.message.data.MessageType;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class UserMessage extends Message {

    @Valid
    @NotNull
    private final Token data;

    @JsonCreator
    public UserMessage(@JsonProperty("sequence_id") String sequenceId,
                       @JsonProperty("data") Token data
    ) {
        super(MessageType.CUSTOMER_API_TOKEN, sequenceId);
        this.data = data;
    }
}
