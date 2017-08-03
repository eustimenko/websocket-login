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

    private static final String SEND_DESTINATION = "/queue/reply";

    private final MessageService messageService;

    @Autowired
    public AuthController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("auth")
    @SendTo(SEND_DESTINATION)
    public Message auth(@Payload(required = false) String received) {
        logger.info("Received: {}", received);
        final Message sent = messageService.prepareMessage(received);
        logger.info("Sent: {}", sent);
        return sent;
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @SendTo(SEND_DESTINATION)
    public ErrorMessage handleMethodArgumentNotValidException(Exception e) {
        return handleException(e, ErrorMessage.nullMessageError());
    }

    @MessageExceptionHandler({JsonMappingException.class, MessageConvertingException.class})
    @SendTo(SEND_DESTINATION)
    public ErrorMessage handleJsonMappingException(Exception e) {
        return handleException(e, ErrorMessage.typeError());
    }

    @MessageExceptionHandler({IncorrectDataException.class})
    @SendTo(SEND_DESTINATION)
    public ErrorMessage handleIncorrectDataException(IncorrectDataException e) {
        return handleException(e, ErrorMessage.dataError(e.sequenceId));
    }

    @MessageExceptionHandler({CustomerNotFoundException.class})
    @SendTo(SEND_DESTINATION)
    public ErrorMessage handleCustomerNotFoundException(CustomerNotFoundException e) {
        return handleException(e, ErrorMessage.customerNotFoundError(e.sequenceId));
    }

    private ErrorMessage handleException(Exception e, ErrorMessage em) {
        logger.debug("{}", e);
        logger.info("Sent: {}", em);
        return em;
    }
}
