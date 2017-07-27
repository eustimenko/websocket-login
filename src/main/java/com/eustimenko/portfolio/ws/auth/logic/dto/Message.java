package com.eustimenko.portfolio.ws.auth.logic.dto;

import com.fasterxml.jackson.annotation.*;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = LoginMessage.class, name = Message.Types.LOGIN_CUSTOMER),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = Message.Types.CUSTOMER_ERROR),
        @JsonSubTypes.Type(value = SuccessMessage.class, name = Message.Types.CUSTOMER_API_TOKEN)
})
public abstract class Message<DATA_TYPE> implements Serializable {

    @JsonProperty("sequence_id")
    final String sequenceId;
    final DATA_TYPE data;

    @JsonCreator
    Message(@JsonProperty("sequence_id") String sequenceId, @JsonProperty("data") DATA_TYPE data) {
        this.sequenceId = sequenceId;
        this.data = data;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public DATA_TYPE getData() {
        return data;
    }

    @JsonIgnore
    boolean hasNoSequence() {
        return StringUtils.isEmpty(sequenceId);
    }

    public String toString() {
        return sequenceId + ":" + data.getClass();
    }

    public interface Types {

        String LOGIN_CUSTOMER = "LOGIN_CUSTOMER";
        String CUSTOMER_API_TOKEN = "CUSTOMER_API_TOKEN";
        String CUSTOMER_ERROR = "CUSTOMER_ERROR";
    }
}
