package com.eustimenko.portfolio.ws.auth.logic.service;

import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.dto.type.AuthCredentials;
import com.eustimenko.portfolio.ws.auth.logic.exception.CustomerNotFoundException;
import com.eustimenko.portfolio.ws.auth.logic.helper.TokenEncryptor;
import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import com.eustimenko.portfolio.ws.auth.persistent.repository.*;
import org.hibernate.Hibernate;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class DefaultMessageService implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageService.class);

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenEncryptor generator;

    @Autowired
    public DefaultMessageService(UserRepository userRepository, TokenRepository tokenRepository, TokenEncryptor generator) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.generator = generator;
    }

    @Transactional
    public Message prepareMessage(String s) {
        final LoginMessage message = LoginMessage.of(s);
        final String sequence = message.getSequenceId();
        final AuthCredentials credentials = message.getData();

        try {
            return processCredentials(sequence, credentials);
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException(sequence);
        }
    }

    private Message processCredentials(String sequence, AuthCredentials ac) {
        final User user = getUserByEmail(ac.getEmail());

        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String raw = ac.getPassword();
        final String encoded = user.getPassword();
        if (passwordEncoder.matches(raw, encoded)) {
            logger.info("User {} is authorized", user.toString());

            return SuccessMessage.of(sequence, com.eustimenko.portfolio.ws.auth.logic.dto.type.Token.of(getActualUserToken(user)));
        } else {
            throw new NoSuchElementException();
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    private Token getActualUserToken(User user) {
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
