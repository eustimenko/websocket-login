package com.eustimenko.services.auth.controller;

import com.eustimenko.services.auth.message.*;
import com.eustimenko.services.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Slf4j
@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @MessageMapping("/login")
    @SendTo("/topic/logged")
    public Message auth(@Valid LoginMessage message) {
        log.debug("{}", message);
        final Message response = authService.auth(message);
        log.debug("{}", response);
        return response;
    }
}
