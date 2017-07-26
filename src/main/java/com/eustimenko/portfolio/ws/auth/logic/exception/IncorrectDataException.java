package com.eustimenko.portfolio.ws.auth.logic.exception;

public class IncorrectDataException extends SequencedMessageException {

    public IncorrectDataException(String sequenceId) {
        super(sequenceId);
    }
}
