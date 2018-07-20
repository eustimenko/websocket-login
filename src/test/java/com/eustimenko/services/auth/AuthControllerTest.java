package com.eustimenko.services.auth;

import com.eustimenko.services.auth.entity.User;
import com.eustimenko.services.auth.message.*;
import com.eustimenko.services.auth.message.data.MessageType;
import com.eustimenko.services.auth.message.handler.SuccessMessageHandler;
import org.junit.*;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

public class AuthControllerTest extends WebControllerBaseTest {

    private CompletableFuture<UserMessage> completableFuture;

    @Before
    public void setup() {
        repository.deleteAll();
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/auth";
    }

    @Test
    public void testInsertAndGetValidUser() throws InterruptedException, ExecutionException, TimeoutException {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final User user = new User("user@user.com", passwordEncoder.encode("withInsertedPassword"));
        repository.save(user);

        createAndSendToWebSocket(new LoginMessage("123", new User("user@user.com", "withInsertedPassword")));

        final UserMessage result = completableFuture.get(10, SECONDS);

        assertNotNull(result);
        assertEquals(MessageType.CUSTOMER_API_TOKEN, result.getType());
        assertEquals("123", result.getSequenceId());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getValue());
        assertNotNull(result.getData().getExpirationDate());
        // TODO: fix @JsonIgnore https://stackoverflow.com/questions/49940537/jackson-could-not-ignore-properties
//        assertNull(result.getData().getCreated());
//        assertNull(result.getData().getUserEmail());
    }

    private void createAndSendToWebSocket(LoginMessage message) throws InterruptedException, ExecutionException, TimeoutException {
        final StompSession stompSession = getNewSession();

        stompSession.subscribe(SUBSCRIBE_LOGIN_ENDPOINT, new SuccessMessageHandler(completableFuture));
        stompSession.send(SEND_LOGIN_ENDPOINT, message);
    }

    @Test
    public void sendMultipleRequests() throws InterruptedException, ExecutionException, TimeoutException {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final User user = new User("user@user.com", passwordEncoder.encode("withInsertedPassword"));
        repository.save(user);

        final ExecutorService threads = Executors.newFixedThreadPool(USERS_NUMBER);
        final List<Callable<Void>> torun = new ArrayList<>(USERS_NUMBER);

        for (int i = 0; i < USERS_NUMBER; i++) {
            torun.add(() -> {
                final User data = new User("user@user.com", "withInsertedPassword");
                final LoginMessage message = new LoginMessage("123", data);
                createAndSendToWebSocket(message);
                return null;
            });
        }

        threads.invokeAll(torun);
        threads.shutdown();
    }
}
