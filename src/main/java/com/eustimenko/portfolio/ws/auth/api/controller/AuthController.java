package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.exception.*;
import com.eustimenko.portfolio.ws.auth.logic.service.MessageService;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String SOURCE = "/auth";
    private static final String DESTINATION = "/topic/customer";

    private final MessageService messageService;

    @Autowired
    public AuthController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping(SOURCE)
    @SendTo(DESTINATION)
    public Message auth(String received) throws MethodArgumentNotValidException, IllegalArgumentException {
        logger.info("Received: {}", received);
        final Message sent = messageService.prepareMessage(received);
        logger.info("Sent: {}", sent);
        return sent;
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @SendTo(DESTINATION)
    public Message handleMethodArgumentNotValidException(Exception e) {
        logger.debug("handleMethodArgumentNotValidException: ", e);
        return ErrorMessage.nullMessageError();
    }

    @MessageExceptionHandler({JsonMappingException.class, MessageConvertingException.class})
    @SendTo(DESTINATION)
    public Message handleJsonMappingException(Exception e) {
        logger.debug("handleJsonMappingException: ", e);
        return ErrorMessage.typeError();
    }

    @MessageExceptionHandler({IncorrectDataException.class})
    @SendTo(DESTINATION)
    public Message handleIncorrectDataException(IncorrectDataException e) {
        logger.debug("handleIncorrectDataException: ", e);
        return ErrorMessage.dataError(e.sequenceId);
    }

    @MessageExceptionHandler({CustomerNotFoundException.class})
    @SendTo(DESTINATION)
    public Message handleCustomerNotFoundException(CustomerNotFoundException e) {
        logger.debug("handleCustomerNotFoundException: ", e);
        return ErrorMessage.customerNotFoundError(e.sequenceId);
    }
}
