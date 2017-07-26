package com.eustimenko.portfolio.ws.auth.api.controller;

import com.eustimenko.portfolio.ws.auth.logic.dto.*;
import com.eustimenko.portfolio.ws.auth.logic.dto.type.AuthCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTestHelper {

    private final ObjectMapper mapper = new ObjectMapper();

    String invalidType() throws JsonProcessingException {
        return mapper.writeValueAsString(ErrorMessage.nullMessageError());
    }

    String empty() throws JsonProcessingException {
        return mapper.writeValueAsString(new LoginMessage("", AuthCredentials.of("", "")));
    }

    String invalidBody() throws JsonProcessingException {
        return mapper.writeValueAsString(new LoginMessage("customSequence", AuthCredentials.of("", "")));
    }

    String nonExistingEmail() throws JsonProcessingException {
        return mapper.writeValueAsString(new LoginMessage("customSequence", AuthCredentials.of("nonExistingEmail", "mandatoryPassword")));
    }

    String invalidPassword() throws JsonProcessingException {
        return mapper.writeValueAsString(
                new LoginMessage("$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu",
                        AuthCredentials.of("test@test.com", "nonMandatoryPassword")));
    }

    String valid() throws JsonProcessingException {
        return mapper.writeValueAsString(
                new LoginMessage("$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu",
                        AuthCredentials.of("test@test.com", "password")));
    }

    ErrorMessage getMessageAsError(String m) {
        return mapper.convertValue(m, ErrorMessage.class);
    }

    SuccessMessage getMessageAsSuccess(String m) {
        return mapper.convertValue(m, SuccessMessage.class);
    }
}
