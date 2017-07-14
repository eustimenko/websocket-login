package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.request.*;
import com.eustimenko.portfolio.ws.auth.api.request.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.service.UserService;
import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Controller
public class AuthController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @MessageMapping("/auth")
    @SendTo("/topic")
    public Message auth(LoginMessage message) throws NoSuchElementException {

        if (message == null || message.hasNoSequence()) {
            return ErrorMessage.nullMessageError();
        }

        final String sequenceId = message.getSequenceId();
        if (message.isIncorrect()) {
            return ErrorMessage.typeError(sequenceId);
        }

        if (message.hasNoData()) {
            return ErrorMessage.dataError(sequenceId);
        }

        try {
            return processCredentials(message.getData(), sequenceId);
        } catch (NoSuchElementException e) {
            return processError(sequenceId);
        }
    }

    private Message processCredentials(LoginCredentials credentials, String s){
        final User user = userService.getUserByEmail(credentials.getEmail());
        if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return new SuccessMessage(new Token(userService.getActualUserToken(user)), s);
        } else {
            return ErrorMessage.passwordError(s);
        }
    }

    private Message processError(String s){
        return ErrorMessage.customerNotFoundError(s);
    }
}
