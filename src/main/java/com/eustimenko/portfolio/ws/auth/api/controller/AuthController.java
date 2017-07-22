package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.exception.*;
import com.eustimenko.portfolio.ws.auth.api.request.*;
import com.eustimenko.portfolio.ws.auth.api.request.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.service.UserService;
import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import com.fasterxml.jackson.databind.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.NoSuchElementException;

//TODO: move exception handling into advice
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = new ObjectMapper();
    }

    @MessageMapping("/auth/")
    @SendTo("/topic/auth/")
    public Message auth(String s) throws MethodArgumentNotValidException, IllegalArgumentException {

        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException();
        }

        final LoginMessage message;
        try {
            message = mapper.readValue(s, LoginMessage.class);
        } catch (IllegalArgumentException | IOException e) {
            throw new MessageConvertingException(e);
        }

        if (message.hasNoSequence()) {
            throw new IllegalArgumentException();
        }

        final String sequenceId = message.getSequenceId();
        if (message.hasNoData()) {
            throw new IncorrectDataException(sequenceId);
        }

        try {
            return processCredentials(message.getData(), sequenceId);
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException(sequenceId);
        }
    }

    private Message processCredentials(LoginCredentials credentials, String s) {
        final User user = userService.getUserByEmail(credentials.getEmail());
        if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return new SuccessMessage(s, new Token(userService.getActualUserToken(user)));
        } else {
            return ErrorMessage.passwordError(s);
        }
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public Message handleMethodArgumentNotValidException(Exception e) {
        logger.debug("handleMethodArgumentNotValidException: ", e);
        return ErrorMessage.nullMessageError();
    }

    @MessageExceptionHandler({JsonMappingException.class, MessageConvertingException.class})
    public Message handleJsonMappingException(Exception e) {
        logger.debug("handleJsonMappingException: ", e);
        return ErrorMessage.typeError();
    }

    @MessageExceptionHandler({IncorrectDataException.class})
    public Message handleIncorrectDataException(IncorrectDataException e) {
        logger.debug("handleIncorrectDataException: ", e);
        return ErrorMessage.dataError(e.sequenceId);
    }

    @MessageExceptionHandler({CustomerNotFoundException.class})
    public Message handleCustomerNotFoundException(CustomerNotFoundException e) {
        logger.debug("handleCustomerNotFoundException: ", e);
        return ErrorMessage.customerNotFoundError(e.sequenceId);
    }
}
