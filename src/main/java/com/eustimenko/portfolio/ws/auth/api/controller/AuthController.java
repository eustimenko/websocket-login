package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.configuration.WebSocketConfiguration;
import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.exception.*;
import com.eustimenko.portfolio.ws.auth.logic.service.MessageService;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String SOURCE = "auth";
    private static final String DESTINATION = "/queue.reply";

    private final MessageService messageService;

    @Autowired
    public AuthController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping(SOURCE)
    @SubscribeMapping(DESTINATION)
    public Message auth(@Payload(required = false) String received, SimpMessageHeaderAccessor accessor) {
        logger.info("Received: {}", received);

        final String sessionId = accessor.getSessionAttributes().get(WebSocketConfiguration.SESSION_ID_ATTRIBUTE_NAME).toString();
        logger.info("Session ID: {}", sessionId);
        accessor.setSessionId(sessionId);

        final Message sent = messageService.prepareMessage(received);
        logger.info("Sent: {}", sent);
        return sent;
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ErrorMessage handleMethodArgumentNotValidException(Exception e) {
        return handleException(e, ErrorMessage.nullMessageError());
    }

    @MessageExceptionHandler({JsonMappingException.class, MessageConvertingException.class})
    public ErrorMessage handleJsonMappingException(Exception e) {
        return handleException(e, ErrorMessage.typeError());
    }

    @MessageExceptionHandler({IncorrectDataException.class})
    public ErrorMessage handleIncorrectDataException(IncorrectDataException e) {
        return handleException(e, ErrorMessage.dataError(e.sequenceId));
    }

    @MessageExceptionHandler({CustomerNotFoundException.class})
    public ErrorMessage handleCustomerNotFoundException(CustomerNotFoundException e) {
        return handleException(e, ErrorMessage.customerNotFoundError(e.sequenceId));
    }

    private ErrorMessage handleException(Exception e, ErrorMessage em) {
        logger.debug("{}", e);
        logger.info("Sent: {}", em);
        return em;
    }
}
