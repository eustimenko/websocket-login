package com.eustimenko.services.auth.service;

import com.eustimenko.services.auth.entity.Token;
import com.eustimenko.services.auth.helper.TokenEncryptor;
import com.eustimenko.services.auth.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DefaultTokenService implements TokenService {

    private static final long TOKEN_EXPIRED_MINS_PERIOD = 15;

    private final TokenEncryptor encryptor;
    private final TokenRepository tokenRepository;

    @Autowired
    public DefaultTokenService(TokenEncryptor encryptor, TokenRepository tokenRepository) {
        this.encryptor = encryptor;
        this.tokenRepository = tokenRepository;
    }

    public Token newToken(String email, String password) {
        final String encrypted = encryptor.encrypt(email, password);
        final Token token = new Token(encrypted,
                LocalDateTime.now().plus(TOKEN_EXPIRED_MINS_PERIOD, ChronoUnit.MINUTES), email);

        return tokenRepository.save(token);
    }
}
