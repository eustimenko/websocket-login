package com.eustimenko.portfolio.ws.auth.api.request;

import com.eustimenko.portfolio.ws.auth.api.request.dto.Token;
import com.eustimenko.portfolio.ws.auth.api.request.type.TYPE_OF_MESSAGE;

public class SuccessMessage extends Message {

    private Token data;

    public SuccessMessage(Token data, String sequenceId) {
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
