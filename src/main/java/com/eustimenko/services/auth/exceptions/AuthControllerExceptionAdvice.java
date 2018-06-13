package com.eustimenko.services.auth.exceptions;

import com.eustimenko.services.auth.message.*;
import com.eustimenko.services.auth.message.data.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class AuthControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    @MessageExceptionHandler(UserNotFoundException.class)
    @SendToUser("/queue/errors")
    public Message handleUserNotFoundException(UserNotFoundException ex) {
        log.error("{}", ex.getClass().getName());
        final Message response = new ErrorMessage(ex.data, ErrorType.CUSTOMER_NOT_FOUND);
        log.debug("Sent to /topic/logged {}", response);
        return response;
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser("/queue/errors")
    public Message handleArgumentsValidationException(MethodArgumentNotValidException ex) {
        log.error("{}", ex.getClass().getName());
        final Message response = new ErrorMessage(ErrorType.INCORRECT_REQUEST_FORMAT);
        log.debug("Sent to /topic/logged {}", response);
        return response;
    }
}
