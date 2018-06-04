package com.eustimenko.services.auth.message.handler;

import com.eustimenko.services.auth.message.UserMessage;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class SuccessMessageHandler implements StompFrameHandler {

    private final CompletableFuture<UserMessage> completableFuture;

    public SuccessMessageHandler(CompletableFuture<UserMessage> completableFuture) {
        this.completableFuture = completableFuture;
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return UserMessage.class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        final UserMessage data = (UserMessage) o;
        completableFuture.complete(data);
    }
}
