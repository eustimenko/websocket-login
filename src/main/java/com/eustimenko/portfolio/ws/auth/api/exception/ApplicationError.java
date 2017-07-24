package com.eustimenko.portfolio.ws.auth.api.exception;

public class ApplicationError extends SequencedMessageException {

    public ApplicationError(Throwable cause, String sequenceId) {
        super(cause, sequenceId);
    }
}
