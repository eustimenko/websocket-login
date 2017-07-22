package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.request.*;
import com.eustimenko.portfolio.ws.auth.api.request.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.service.UserService;
import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;
    private String URL;

    private static final String SEND_LOGIN_ENDPOINT = "/app/auth/";
    private static final String SUBSCRIBE_LOGIN_ENDPOINT = "/topic/auth/";

    private CompletableFuture<Message> completableFuture;
    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Before
    public void setup() throws InterruptedException, ExecutionException, TimeoutException {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/login";

        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_LOGIN_ENDPOINT, new MessageStompFrameHandler());
    }

    private List<Transport> createTransportClient() {
        final List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    @After
    public void tearDown() {
        stompSession.disconnect();
        if (stompClient.isRunning()) {
            stompClient.stop();
        }
    }

    @Test
    public void sendNull() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, null);

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.MESSAGE_IS_NULL);
    }

    @Test
    public void sendEmpty() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, "");

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.MESSAGE_IS_NULL);
    }

    @Test
    public void sendIncorrectTypeOfMessage() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, mapper.writeValueAsString(ErrorMessage.nullMessageError()));

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.TYPE_IS_INCORRECT);
    }

    @Test
    public void sendIncorrectTypeOfArgument() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, ErrorMessage.nullMessageError());

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.TYPE_IS_INCORRECT);
    }

    @Test
    public void sendNoSequence() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, mapper.writeValueAsString(new LoginMessage("", new LoginCredentials("", ""))));

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.MESSAGE_IS_NULL);
    }

    @Test
    public void sendNoData() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, mapper.writeValueAsString(new LoginMessage("customSequence", new LoginCredentials("", ""))));

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.DATA_IS_INCORRECT);
    }

    @Test
    public void sendNonExistingEmail() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, mapper.writeValueAsString(new LoginMessage("customSequence", new LoginCredentials("nonExistingEmail", "mandatoryPassword"))));

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.CUSTOMER_NOT_FOUND);
    }

    @Test
    @Transactional
    @Ignore("Correct mocking of `getUserByEmail()`")
    public void sendExistingEmailWithIncorrectPassword() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        given(userService.getUserByEmail("existing@gmail.com")).willReturn(new User("existing@gmail.com", "mandatoryPassword"));

        stompSession.send(SEND_LOGIN_ENDPOINT, mapper.writeValueAsString(new LoginMessage("existing@mail.com", new LoginCredentials("existing@mail.com", "nonMandatoryPassword"))));

        Message result = completableFuture.get(1, SECONDS);
        assertNotNull(result);

        result = mapper.convertValue(result, ErrorMessage.class);
        assertEquals(result.getData(), ERROR.PASSWORD_IS_INCORRECT);
    }

    private class MessageStompFrameHandler implements StompFrameHandler {
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Message.class;
        }

        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((Message) o);
        }
    }
}
