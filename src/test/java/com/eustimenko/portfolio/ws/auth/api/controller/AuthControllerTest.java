package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.dto.type.*;
import org.junit.*;
import org.junit.runner.RunWith;
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
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    private static final long TIMEOUT = 1;
    @LocalServerPort
    private int port;
    private String URL;

    private static final String SEND_LOGIN_ENDPOINT = "/app/auth";
    private static final String SUBSCRIBE_LOGIN_ENDPOINT = "/topic/customer";

    private CompletableFuture<String> completableFuture;
    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    private AuthControllerTestHelper helper = new AuthControllerTestHelper();

    @Before
    public void setup() throws InterruptedException, ExecutionException, TimeoutException {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/login";

        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(TIMEOUT, SECONDS);

        stompSession.subscribe(SUBSCRIBE_LOGIN_ENDPOINT, new MessageStompFrameHandler(completableFuture));
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
    public void sendAny() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, "hello");

        String result = getResult();
        assertNotNull(result);
    }

    @Test
    public void sendNull() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, null);

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.MESSAGE_IS_NULL, result.getData());
    }

    private String getResult() throws InterruptedException, ExecutionException, TimeoutException {
        return completableFuture.get(TIMEOUT, SECONDS);
    }

    @Test
    public void sendEmpty() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, "");

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.MESSAGE_IS_NULL, result.getData());
    }

    @Test
    public void sendIncorrectTypeOfMessage() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, helper.invalidType());

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.FORMAT_IS_INCORRECT, result.getData());
    }

    @Test
    public void sendIncorrectTypeOfArgument() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, ErrorMessage.nullMessageError());

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.FORMAT_IS_INCORRECT, result.getData());
    }

    @Test
    public void sendNoSequence() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, helper.empty());

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.MESSAGE_IS_NULL, result.getData());
    }

    @Test
    public void sendNoData() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, helper.invalidBody());

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.DATA_IS_INCORRECT, result.getData());
    }

    @Test
    public void sendNonExistingEmail() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, helper.nonExistingEmail());

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.CUSTOMER_NOT_FOUND, result.getData());
    }

    @Test
    public void sendExistingEmailWithIncorrectPassword() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, helper.invalidPassword());

        final Message<ERROR> result = helper.getMessageAsError(getResult());

        assertEquals(ERROR.CUSTOMER_NOT_FOUND, result.getData());
    }

    @Test
    @Transactional
    @Ignore
    public void sendExistingEmailWithCorrectPassword() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        stompSession.send(SEND_LOGIN_ENDPOINT, helper.valid());

        final Message<Token> result = helper.getMessageAsSuccess(getResult());
        final Token token = result.getData();

        assertNotNull(token);
        assertNotEquals("", token.getApiToken());
    }
}
