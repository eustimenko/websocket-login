package com.eustimenko.services.auth;

import com.eustimenko.services.auth.entity.*;
import com.eustimenko.services.auth.message.*;
import com.eustimenko.services.auth.message.data.*;
import com.eustimenko.services.auth.message.handler.ErrorMessageHandler;
import org.junit.*;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

public class AuthControllerValidationTest extends WebControllerBaseTest {

    private CompletableFuture<ErrorMessage> completableFuture;

    @Before
    public void setup() {
        repository.deleteAll();
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/auth";
    }

    @Test
    public void validateRequestType() throws InterruptedException, ExecutionException, TimeoutException {
        createStompSession(new User("", ""));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals(ErrorType.INCORRECT_REQUEST_FORMAT, result.getData());
    }

    private void createStompSession(Object o) throws InterruptedException, ExecutionException, TimeoutException {
        final StompSession stompSession = getNewSession();

        stompSession.subscribe(SUBSCRIBE_ERROR_ENDPOINT, new ErrorMessageHandler(completableFuture));
        stompSession.send(SEND_LOGIN_ENDPOINT, o);
    }

    @Test
    public void validateRequestMessageType() throws InterruptedException, ExecutionException, TimeoutException {
        createStompSession(new UserMessage("123", new Token("123", LocalDateTime.now(), "user@user.com")));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals(ErrorType.INCORRECT_REQUEST_FORMAT, result.getData());
    }

    @Test
    public void validateEmail() throws InterruptedException, ExecutionException, TimeoutException {
        createStompSession(new LoginMessage("123", new User("user", "password")));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals(ErrorType.INCORRECT_REQUEST_FORMAT, result.getData());
    }

    @Test
    public void validateEmptyCredentials() throws InterruptedException, ExecutionException, TimeoutException {
        createStompSession(new LoginMessage("", new User("user@user.com", "password")));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals(ErrorType.INCORRECT_REQUEST_FORMAT, result.getData());
    }

    @Test
    public void validateEmptySequence() throws InterruptedException, ExecutionException, TimeoutException {
        createStompSession(new LoginMessage("", new User("user@user.com", "password")));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals(ErrorType.INCORRECT_REQUEST_FORMAT, result.getData());
    }

    @Test
    public void testCustomerNotFound() throws InterruptedException, ExecutionException, TimeoutException {
        createStompSession(new LoginMessage("123", new User("user2@user.com", "withInsertedPassword")));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals("123", result.getSequenceId());
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, result.getData());
    }

    @Test
    public void testCustomerPasswordNotMatch() throws InterruptedException, ExecutionException, TimeoutException {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final User user = new User("user2@user.com", passwordEncoder.encode("withInsertedPassword"));
        repository.save(user);

        createStompSession(new LoginMessage("123", new User("user2@user.com", "wrongPassword")));

        final ErrorMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_ERROR, result.getType());
        assertEquals("123", result.getSequenceId());
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, result.getData());
    }
}
