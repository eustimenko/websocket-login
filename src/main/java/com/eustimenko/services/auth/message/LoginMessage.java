package com.eustimenko.services.auth.message;

import com.eustimenko.services.auth.entity.User;
import com.eustimenko.services.auth.message.data.MessageType;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class LoginMessage extends Message {

    @Valid
    @NotNull
    private final User data;

    @JsonCreator
    public LoginMessage(@JsonProperty("sequence_id") String sequenceId,
                        @JsonProperty("data") User data) {
        super(MessageType.LOGIN_CUSTOMER, sequenceId);
        this.data = data;
    }
}
