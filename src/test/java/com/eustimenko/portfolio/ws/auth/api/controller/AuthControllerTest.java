package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.dto.type.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.*;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    private static final long TIMEOUT = 1;
    @LocalServerPort
    private int port;

    private CompletableFuture<Message> result;
    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    private AuthControllerTestHelper helper = new AuthControllerTestHelper();

    @Before
    public void setup() {
        result = new CompletableFuture<>();
        final String URL = "ws://localhost:" + port + "/login";

        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        try {
            stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        stompSession.subscribe(AuthController.DESTINATION, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders headers) {
                logger.info("Handle getPayloadType()");
                return Message.class;
            }

            public void handleFrame(StompHeaders headers, Object payload) {
                logger.info("Handle handleFrame(): {}", payload);
                result.complete((Message) payload);
            }
        });
    }

    private List<Transport> createTransportClient() {
        final List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    @After
    public void tearDown() {
        if (stompSession.isConnected()) stompSession.disconnect();
        if (stompClient.isRunning()) stompClient.stop();
    }

    @Test
    public void sendAny() {
        sendToSocket("hello");

        Message result = getResult();
        assertNotNull(result);
    }

    private void sendToSocket(Object o) {
        stompSession.send(AuthController.SOURCE, o);
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
    @Ignore
    public void sendExistingEmailWithCorrectPassword() throws JsonProcessingException {
        sendToSocket(helper.valid());

        final Message<Token> result = helper.getMessageAsSuccess(getResult());
        final Token token = result.getData();

        assertNotNull(token);
        assertNotEquals("", token.getApiToken());
    }
}
