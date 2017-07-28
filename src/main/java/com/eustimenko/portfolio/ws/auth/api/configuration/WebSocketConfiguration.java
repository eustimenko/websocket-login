package com.eustimenko.portfolio.ws.auth.api.configuration;

import com.eustimenko.portfolio.ws.auth.api.configuration.interceptor.HttpHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

    public static final String SESSION_ID_ATTRIBUTE_NAME = "sessionId";

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/", "/topic/");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setPathMatcher(new AntPathMatcher("."));
    }

    protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/login")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpHandshakeInterceptor())
                .withSockJS()
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(30 * 1000);
    }
}
