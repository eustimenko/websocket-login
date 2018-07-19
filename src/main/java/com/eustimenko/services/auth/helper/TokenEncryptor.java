package com.eustimenko.services.auth.helper;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class TokenEncryptor {

    private static final String KEY = "adfh@er394298dsfjfd1§298dfjdfjhdsf93323jnrej";

    public String encrypt(String email, String password) {
        final String text = email + ";" + password;
        return Base64.encodeBase64String(this.xor(text.getBytes(StandardCharsets.UTF_8)));
    }

    private byte[] xor(byte[] input) {
        final byte[] output = new byte[input.length];
        final byte[] secret = KEY.getBytes(StandardCharsets.UTF_8);
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
