package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.request.*;
import com.eustimenko.portfolio.ws.auth.api.request.data.*;
import com.eustimenko.portfolio.ws.auth.logic.service.UserService;
import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

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
            return new ErrorMessage(null, ERROR.MESSAGE_IS_NULL);
        }
        if (!message.isCorrect()) {
            return new ErrorMessage(message.getSequenceId(), ERROR.TYPE_IS_INCORRECT);
        }
        //TODO: hasData()
        if (message.getData() == null) {
            return new ErrorMessage(message.getSequenceId(), ERROR.DATA_IS_INCORRECT);
        }

        final String email = message.getData().getEmail();
        final String password = message.getData().getPassword();

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return new ErrorMessage(message.getSequenceId(), ERROR.DATA_IS_INCORRECT);
        }

        try {
            final User user = userService.getUserByEmail(email);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return new SuccessMessage(message.getSequenceId(), new Token(userService.getActualUserToken(user)));
            } else {
                return new ErrorMessage(message.getSequenceId(), ERROR.PASSWORD_IS_INCORRECT);
            }
        } catch (NoSuchElementException e) {
            return new ErrorMessage(message.getSequenceId(), ERROR.CUSTOMER_NOT_FOUND);
        }
    }
}
