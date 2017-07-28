package com.eustimenko.portfolio.ws.auth.api.configuration.interceptor;

import com.eustimenko.portfolio.ws.auth.api.configuration.WebSocketConfiguration;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            final ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            final HttpSession session = servletRequest.getServletRequest().getSession();
            attributes.put(WebSocketConfiguration.SESSION_ID_ATTRIBUTE_NAME, session.getId());
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
