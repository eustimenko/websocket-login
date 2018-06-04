package com.eustimenko.services.auth.message.handler;

import com.eustimenko.services.auth.message.ErrorMessage;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class ErrorMessageHandler implements StompFrameHandler {

    private final CompletableFuture<ErrorMessage> completableFuture;

    public ErrorMessageHandler(CompletableFuture<ErrorMessage> completableFuture) {
        this.completableFuture = completableFuture;
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return ErrorMessage.class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        final ErrorMessage data = (ErrorMessage) o;
        completableFuture.complete(data);
    }
}