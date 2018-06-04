package com.eustimenko.services.auth.message;

import com.eustimenko.services.auth.message.data.MessageType;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@ToString
public class Message {

    @NotNull
    protected final MessageType type;
    @NotEmpty
    protected final String sequenceId;

    @JsonCreator
    Message(@JsonProperty("type") MessageType type, @JsonProperty("sequence_id") String sequenceId) {
        this.type = type;
        this.sequenceId = sequenceId;
    }
}
