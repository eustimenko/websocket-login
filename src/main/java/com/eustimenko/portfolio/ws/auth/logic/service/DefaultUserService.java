package com.eustimenko.portfolio.ws.auth.logic.service;

import com.eustimenko.portfolio.ws.auth.logic.helper.TokenEncryptor;
import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import com.eustimenko.portfolio.ws.auth.persistent.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenEncryptor generator;

    @Autowired
    public DefaultUserService(UserRepository userRepository, TokenRepository tokenRepository, TokenEncryptor generator) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.generator = generator;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public Token getActualUserToken(User user) {
        return tokenRepository.getTokenByUser(user.getEmail())
                .map(t -> expireAndGetNew(t, user))
                .orElseGet(() -> newToken(user));
    }

    private Token newToken(User user) {
        final Token token = tokenRepository.save(generator.generateNewToken(user));
        Hibernate.initialize(token.getUser());
        return token;
    }

    private Token expireAndGetNew(Token token, User user) {
        token.setExpiredDate(LocalDateTime.now());
        tokenRepository.save(token);
        return newToken(user);
    }
}
