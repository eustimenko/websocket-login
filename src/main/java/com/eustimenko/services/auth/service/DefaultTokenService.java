package com.eustimenko.services.auth.service;

import com.eustimenko.services.auth.configuration.External;
import com.eustimenko.services.auth.entity.Token;
import com.eustimenko.services.auth.helper.TokenEncryptor;
import com.eustimenko.services.auth.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DefaultTokenService implements TokenService {

    private final External properties;
    private final TokenEncryptor encryptor;
    private final TokenRepository tokenRepository;

    @Autowired
    public DefaultTokenService(TokenEncryptor encryptor, TokenRepository tokenRepository, External properties) {
        this.encryptor = encryptor;
        this.tokenRepository = tokenRepository;
        this.properties = properties;
    }

    private LocalDateTime tokenExpiredTime;

    @PostConstruct
    public void configure() {
        this.tokenExpiredTime = LocalDateTime.now().plus(properties.getExpired(), ChronoUnit.MINUTES);
    }

    public Token newToken(String email, String password) {
        final String encrypted = encryptor.encrypt(email, password);
        final Token token = new Token(encrypted, tokenExpiredTime, email);

        return tokenRepository.save(token);
    }
}
