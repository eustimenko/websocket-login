package com.eustimenko.portfolio.ws.auth.logic.helper;

import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class TokenEncryptor {

    private static final String KEY = "adfh@fifiksrufdhbbkbkfhbjyjdbxuecm";

    public Token generateNewToken(User user) {
        return new Token(generateNewToken(user.getEmail() + ";" + user.getPassword()), user);
    }

    private String generateNewToken(String text) {
        return Base64.encodeBase64String(this.xor(text.getBytes()));
    }

    private byte[] xor(final byte[] input) {
        final byte[] output = new byte[input.length];
        final byte[] secret = KEY.getBytes();

        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            spos += 1;
            if (spos >= secret.length) {
                spos = 0;
            }
        }

        return output;
    }
}
