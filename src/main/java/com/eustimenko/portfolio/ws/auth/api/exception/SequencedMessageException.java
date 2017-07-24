package com.eustimenko.portfolio.ws.auth.api.exception;

class SequencedMessageException extends RuntimeException {

    public final String sequenceId;

    SequencedMessageException(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    SequencedMessageException(Throwable cause, String sequenceId) {
        super(cause);
        this.sequenceId = sequenceId;
    }
}
