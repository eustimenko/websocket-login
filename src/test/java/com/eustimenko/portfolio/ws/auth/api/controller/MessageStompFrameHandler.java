package com.eustimenko.portfolio.ws.auth.api.controller;

import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class MessageStompFrameHandler implements StompFrameHandler {

    private final CompletableFuture<String> completableFuture;

    MessageStompFrameHandler(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return String.class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        completableFuture.complete((String) o);
    }
}
