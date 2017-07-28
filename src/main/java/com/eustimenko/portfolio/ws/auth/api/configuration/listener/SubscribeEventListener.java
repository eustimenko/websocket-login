package com.eustimenko.portfolio.ws.auth.api.configuration.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor.wrap(event.getMessage());
    }
}
