package com.eustimenko.services.auth.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@ToString
@JsonIgnoreProperties(value = {"userEmail", "created"}, allowSetters = true)
public class Token {

    @Id
    @NotEmpty
    private final String value;

    @NotEmpty
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime expirationDate;

    @Email
    @NotEmpty
    private final String userEmail;

    @NotEmpty
    @JsonProperty("created")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime created;

    @JsonCreator
    public Token(@JsonProperty("api_token") String value,
                 @JsonProperty("api_token_expiration_date") LocalDateTime expirationDate,
                 @JsonProperty("userEmail") String userEmail
    ) {
        this.value = value;
        this.expirationDate = expirationDate;
        this.userEmail = userEmail;
        this.created = LocalDateTime.now();
    }
}
