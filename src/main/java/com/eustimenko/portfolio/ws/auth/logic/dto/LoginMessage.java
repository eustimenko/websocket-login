package com.eustimenko.portfolio.ws.auth.logic.dto;

import com.eustimenko.portfolio.ws.auth.logic.dto.type.AuthCredentials;
import com.eustimenko.portfolio.ws.auth.logic.exception.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class LoginMessage extends Message<AuthCredentials> {

    @JsonCreator
    @JsonPropertyOrder({"type", "sequence_id", "data"})
    public LoginMessage(@JsonProperty("sequence_id") String sequenceId, @JsonProperty("data") AuthCredentials data) {
        super(sequenceId, data);
    }

    @JsonIgnore
    private boolean hasNoData() {
        return data == null
                || StringUtils.isEmpty(data.getEmail())
                || StringUtils.isEmpty(data.getPassword());
    }

    public static LoginMessage of(String s) {
        if (StringUtils.isEmpty(s)) throw new IllegalArgumentException();

        final ObjectMapper mapper = new ObjectMapper();
        LoginMessage message;

        try {
            message = mapper.readValue(s, LoginMessage.class);
        } catch (IllegalArgumentException | IOException e) {
            throw new MessageConvertingException(e);
        }

        if (message.hasNoSequence()) throw new IllegalArgumentException();
        if (message.hasNoData()) throw new IncorrectDataException(message.sequenceId);

        return message;
    }
}
