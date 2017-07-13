package com.eustimenko.portfolio.ws.auth.logic.service;

import com.eustimenko.portfolio.ws.auth.logic.helper.TokenEncryptor;
import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import com.eustimenko.portfolio.ws.auth.persistent.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional
public class DefaultUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenEncryptor generator;

    public User getUserByEmail(String email) throws NoSuchElementException {
        return userRepository.findByEmail(email);
    }

    public Token getActualUserToken(User user) {
        Token token = tokenRepository.getTokenByUser(user.getEmail());

        if (token != null && token.isExpired()) {
            expireToken(token);
        } else {
            token = generator.generateNewToken(user);
        }

        token = tokenRepository.save(token);
        Hibernate.initialize(token.getUser());

        return token;
    }

    private void expireToken(Token token) {
        token.setExpiredDate(LocalDateTime.now());
        tokenRepository.save(token);
    }
}
