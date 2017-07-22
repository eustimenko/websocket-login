package com.eustimenko.portfolio.ws.auth.api.exception;

public class IncorrectDataException extends SequencedMessageException {

    public IncorrectDataException(String sequenceId) {
        super(sequenceId);
    }
}
