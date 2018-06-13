package com.eustimenko.services.auth.controller;

import com.eustimenko.services.auth.message.*;
import com.eustimenko.services.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Slf4j
@Controller
public class AuthController {

    private final AuthService authService;
    private final SimpMessagingTemplate messageSender;

    @Autowired
    public AuthController(AuthService authService, SimpMessagingTemplate messageSender) {
        this.authService = authService;
        this.messageSender = messageSender;
    }

    @MessageMapping("/login")
    public void auth(@Valid LoginMessage message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("{}", message);
        final Message response = authService.auth(message);
        log.info("{}", response);
        messageSender.convertAndSendToUser(message.getSequenceId(), "/queue/reply", response);
    }
}
