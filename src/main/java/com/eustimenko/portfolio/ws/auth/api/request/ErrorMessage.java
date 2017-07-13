package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.data.ERROR;
import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;

public class ErrorMessage extends Message {

    private ERROR data;

    public ErrorMessage() {
    }

    public ErrorMessage(String sequenceId, ERROR data) {
        super(TYPE_OF_MESSAGE.CUSTOMER_ERROR, sequenceId);
        this.data = data;
    }

    public ERROR getData() {
        return data;
    }

    public void setData(ERROR data) {
        this.data = data;
    }
}
