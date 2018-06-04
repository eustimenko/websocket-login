package com.eustimenko.services.auth.service;

import com.eustimenko.services.auth.entity.User;
import com.eustimenko.services.auth.exceptions.UserNotFoundException;
import com.eustimenko.services.auth.helper.PasswordEncoder;
import com.eustimenko.services.auth.message.*;
import com.eustimenko.services.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Service
public class DefaultAuthService implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    @Autowired
    public DefaultAuthService(UserRepository repository, PasswordEncoder encoder, TokenService tokenService) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    public Message auth(LoginMessage message) {
        @Valid @NotNull final User data = message.getData();
        @NotEmpty @Email final String email = data.getEmail();
        @NotEmpty final String password = data.getPassword();

        return repository.findByEmail(email)
                .filter(e -> encoder.match(password, e.getPassword()))
                .map(e -> new UserMessage(message.getSequenceId(), tokenService.newToken(email, password)))
                .orElseThrow(() -> new UserNotFoundException(message.getSequenceId()));
    }
}
