package com.eustimenko.services.auth.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public boolean match(String decoded, String encoded) {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        passwordEncoder.encode(decoded);
        return passwordEncoder.matches(decoded, encoded);
    }
}
