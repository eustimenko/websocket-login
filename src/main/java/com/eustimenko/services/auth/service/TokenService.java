package com.eustimenko.services.auth.service;

import com.eustimenko.services.auth.entity.Token;

public interface TokenService {

    Token newToken(String email, String password);
}
