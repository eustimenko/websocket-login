package com.eustimenko.portfolio.ws.auth.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    public static final String SESSION_ID_ATTRIBUTE_NAME = "sessionId";

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app", "/user");
        registry.setUserDestinationPrefix("/user");
        registry.setPathMatcher(new AntPathMatcher("."));
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/login")
                .setAllowedOrigins("*")
                .withSockJS()
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(30 * 1000);
    }
}
