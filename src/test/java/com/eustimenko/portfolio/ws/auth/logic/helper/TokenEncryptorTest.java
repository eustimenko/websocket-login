package com.eustimenko.portfolio.ws.auth.logic.helper;

import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import org.junit.Test;
import org.springframework.util.StringUtils;

import static org.junit.Assert.assertTrue;

public class TokenEncryptorTest {

    private final TokenEncryptor tokenEncryptor = new TokenEncryptor();

    @Test
    public void generateNewToken() throws Exception {
        final User user = new User("user@test.com", "test");
        final Token token = tokenEncryptor.generateNewToken(user);

        assertTrue(token != null && !StringUtils.isEmpty(token.getToken()));
    }
}
