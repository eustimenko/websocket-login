package com.eustimenko.services.auth;

import com.eustimenko.services.auth.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.*;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebControllerBaseTest {

    static final String SUBSCRIBE_ERROR_ENDPOINT = "/topic/error";
    static final String SUBSCRIBE_LOGIN_ENDPOINT = "/user/queue/reply";
    static final String SEND_LOGIN_ENDPOINT = "/app/login";
    static final int USERS_NUMBER = 10;

    @LocalServerPort
    protected int port = 1234;
    String URL;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    protected UserRepository repository;

    StompSession getNewSession() throws InterruptedException, ExecutionException, TimeoutException {
        final WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient
                .connect(URL, new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);
    }

    private List<Transport> createTransportClient() {
        final List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    @Test
    public void testApplicationContextNotNull() {
        assertNotNull(wac);
    }
}
