package com.eustimenko.services.auth.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;

@Getter
@ToString
public class User {

    @Id
    @NotEmpty
    @Email
    private final String email;

    @NotEmpty
    private final String password;

    @JsonCreator
    public User(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }
}
