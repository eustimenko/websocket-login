package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.dto.*;
import com.eustimenko.portfolio.ws.auth.api.dto.type.*;
import com.eustimenko.portfolio.ws.auth.api.exception.*;
import com.eustimenko.portfolio.ws.auth.logic.service.UserService;
import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import com.fasterxml.jackson.databind.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/auth/")
    @SendTo("/topic/auth/")
    public Message auth(String s) throws MethodArgumentNotValidException, IllegalArgumentException {
        final LoginMessage message = getMessage(s);
        final String sequenceId = getSequence(message);

        if (message.hasNoData()) throw new IncorrectDataException(sequenceId);

        return prepareMessage(message, sequenceId);
    }

    private LoginMessage getMessage(String s) {
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException();
        }

        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(s, LoginMessage.class);
        } catch (IllegalArgumentException | IOException e) {
            throw new MessageConvertingException(e);
        }
    }

    private String getSequence(LoginMessage message) {
        if (message.hasNoSequence()) {
            throw new IllegalArgumentException();
        }

        return message.getSequenceId();
    }

    private Message prepareMessage(LoginMessage message, String sequenceId) {
        try {
            final Message sent = processCredentials(message.getData(), sequenceId);
            logger.info("Sent {}", sent.toString());
            return sent;
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException(sequenceId);
        } catch (Exception e) {
            throw new ApplicationError(e, sequenceId);
        }
    }

    private Message processCredentials(AuthCredentials credentials, String s) {
        final User user = userService.getUserByEmail(credentials.getEmail());
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            logger.info("User {} is authorized", user.toString());
            return SuccessMessage.of(s, Token.of(userService.getActualUserToken(user)));
        } else {
            return ErrorMessage.passwordError(s);
        }
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @SendTo("/topic/auth/")
    public Message handleMethodArgumentNotValidException(Exception e) {
        logger.debug("handleMethodArgumentNotValidException: ", e);
        return ErrorMessage.nullMessageError();
    }

    @MessageExceptionHandler({JsonMappingException.class, MessageConvertingException.class})
    @SendTo("/topic/auth/")
    public Message handleJsonMappingException(Exception e) {
        logger.debug("handleJsonMappingException: ", e);
        return ErrorMessage.typeError();
    }

    @MessageExceptionHandler({IncorrectDataException.class})
    @SendTo("/topic/auth/")
    public Message handleIncorrectDataException(IncorrectDataException e) {
        logger.debug("handleIncorrectDataException: ", e);
        return ErrorMessage.dataError(e.sequenceId);
    }

    @MessageExceptionHandler({CustomerNotFoundException.class})
    @SendTo("/topic/auth/")
    public Message handleCustomerNotFoundException(CustomerNotFoundException e) {
        logger.debug("handleCustomerNotFoundException: ", e);
        return ErrorMessage.customerNotFoundError(e.sequenceId);
    }

    @MessageExceptionHandler({ApplicationError.class})
    @SendTo("/topic/auth/")
    public Message handleApplicationError(ApplicationError e) {
        logger.error("APPLICATION ERROR: {}", e);
        return ErrorMessage.applicationError(e.sequenceId);
    }
}
