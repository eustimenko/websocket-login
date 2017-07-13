package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.data.Token;
import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;

public class SuccessMessage extends Message {

    private Token data;

    public SuccessMessage(String sequenceId, Token data) {
        super(TYPE_OF_MESSAGE.CUSTOMER_API_TOKEN, sequenceId);
        this.data = data;
    }

    public Token getData() {
        return data;
    }

    public void setData(Token data) {
        this.data = data;
    }
}
