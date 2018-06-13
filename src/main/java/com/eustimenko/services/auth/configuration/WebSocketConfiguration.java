package com.eustimenko.services.auth.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/auth")
                .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy() {
                    public void upgrade(ServerHttpRequest request, ServerHttpResponse response, String selectedProtocol, List<WebSocketExtension> selectedExtensions, Principal user, WebSocketHandler wsHandler, Map<String, Object> attrs) throws HandshakeFailureException {
                        if (request instanceof ServletServerHttpRequest) {
                            final ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                            final HttpSession session = servletRequest.getServletRequest().getSession();
                            attrs.put("sessionId", session.getId());
                        }
                        super.upgrade(request, response, selectedProtocol, selectedExtensions, user, wsHandler, attrs);
                    }
                }))
                .withSockJS();
    }
}
