package com.eustimenko.services.auth.exceptions;

public class UserNotFoundException extends RuntimeException {

    public final String data;

    public UserNotFoundException(String data) {
        this.data = data;
    }
}
