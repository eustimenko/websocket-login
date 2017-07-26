package com.eustimenko.portfolio.ws.auth.logic.exception;

public class CustomerNotFoundException extends SequencedMessageException {

    public CustomerNotFoundException(String sequenceId) {
        super(sequenceId);
    }
}
