package com.eustimenko.services.auth.message.data;

import lombok.ToString;

@ToString
public enum MessageType {
    LOGIN_CUSTOMER,
    CUSTOMER_API_TOKEN,
    CUSTOMER_ERROR
}
