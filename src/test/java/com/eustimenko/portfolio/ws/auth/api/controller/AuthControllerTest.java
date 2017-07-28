package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.dto.type.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
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

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    private static final long TIMEOUT = 1L;
    private static final String AUTH_END_POINT = "/app/auth";
    private static final String SUBSCRIBED_END_POINT = "/queue/reply";

    private CompletableFuture<Message> result = new CompletableFuture<>();
    private StompSession stompSession;

    private AuthControllerTestHelper helper = new AuthControllerTestHelper();

    @LocalServerPort
    private int port;

    @Before
    public void setup() throws InterruptedException, ExecutionException, TimeoutException {
        final WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports()));

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompSession = stompClient
                .connect("ws://localhost:" + port + "/login", new StompSessionHandlerAdapter() {
                })
                .get(TIMEOUT, SECONDS);

        stompSession.subscribe(SUBSCRIBED_END_POINT, new DefaultStompFrameHandler());
    }

    private List<Transport> transports() {
        final List<Transport> transports = new ArrayList<>(3);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        transports.add(toSimulateLargeNumberOfConcurrent());

        return transports;
    }

    private Transport toSimulateLargeNumberOfConcurrent() {
        final HttpClient httpClient = new HttpClient();
        httpClient.setMaxConnectionsPerDestination(1000);
        httpClient.setExecutor(new QueuedThreadPool(1000));

        return new JettyXhrTransport(httpClient);
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Message.class;
        }

        public void handleFrame(StompHeaders stompHeaders, Object o) {
            result.complete((Message) o);
        }
    }

    @Test
    public void sendAny() {
        sendToSocket("hello");

        Message result = getResult();
        assertNotNull(result);
    }

    private void sendToSocket(Object o) {
        stompSession.send(AUTH_END_POINT, o);
    }

    private Message getResult() {
        try {
            return result.get(TIMEOUT, SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void sendNull() {
        sendToSocket(null);

        assertEquals(ERROR.MESSAGE_IS_NULL, getErrorData());
    }

    private ERROR getErrorData() {
        return helper.getMessageAsError(getResult()).getData();
    }

    @Test
    public void sendEmpty() {
        sendToSocket("");

        assertEquals(ERROR.MESSAGE_IS_NULL, getErrorData());
    }

    @Test
    public void sendIncorrectTypeOfMessage() throws JsonProcessingException {
        sendToSocket(helper.invalidType());

        assertEquals(ERROR.FORMAT_IS_INCORRECT, getErrorData());
    }

    @Test
    public void sendIncorrectTypeOfArgument() {
        sendToSocket(ErrorMessage.nullMessageError());

        assertEquals(ERROR.FORMAT_IS_INCORRECT, getErrorData());
    }

    @Test
    public void sendNoSequence() throws JsonProcessingException {
        sendToSocket(helper.empty());

        assertEquals(ERROR.MESSAGE_IS_NULL, getErrorData());
    }

    @Test
    public void sendNoData() throws JsonProcessingException {
        sendToSocket(helper.invalidBody());

        assertEquals(ERROR.DATA_IS_INCORRECT, getErrorData());
    }

    @Test
    public void sendNonExistingEmail() throws JsonProcessingException {
        sendToSocket(helper.nonExistingEmail());

        assertEquals(ERROR.CUSTOMER_NOT_FOUND, getErrorData());
    }

    @Test
    public void sendExistingEmailWithIncorrectPassword() throws JsonProcessingException {
        sendToSocket(helper.invalidPassword());

        assertEquals(ERROR.CUSTOMER_NOT_FOUND, getErrorData());
    }

    @Test
    @Transactional
    public void sendExistingEmailWithCorrectPassword() throws JsonProcessingException {
        sendToSocket(helper.valid());

        final Message<Token> result = helper.getMessageAsSuccess(getResult());
        final Token token = result.getData();

        assertNotNull(token);
        assertNotEquals("", token.getApiToken());
    }
}
