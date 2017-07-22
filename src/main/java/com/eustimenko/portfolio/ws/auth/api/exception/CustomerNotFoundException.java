package com.eustimenko.portfolio.ws.auth.api.exception;

public class CustomerNotFoundException extends SequencedMessageException {

    public CustomerNotFoundException(String sequenceId) {
        super(sequenceId);
    }
}
