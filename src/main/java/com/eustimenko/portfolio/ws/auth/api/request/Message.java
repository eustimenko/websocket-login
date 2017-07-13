package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class Message implements Serializable {

    TYPE_OF_MESSAGE type;
    private String sequenceId;

    public Message() {
    }

    Message(TYPE_OF_MESSAGE type, String sequenceId) {
        this.type = type;
        this.sequenceId = sequenceId;
    }

    public TYPE_OF_MESSAGE getType() {
        return type;
    }

    public void setType(TYPE_OF_MESSAGE type) {
        this.type = type;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @JsonIgnore
    public boolean hasNoSequence() {
        return StringUtils.isEmpty(sequenceId);
    }
}
