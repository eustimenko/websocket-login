package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.api.dto.Message;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class MessageStompFrameHandler implements StompFrameHandler {

    private final CompletableFuture<Message> completableFuture;

    public MessageStompFrameHandler(CompletableFuture<Message> completableFuture) {
        this.completableFuture = completableFuture;
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return Message.class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        completableFuture.complete((Message) o);
    }
}
