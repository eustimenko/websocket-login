package com.eustimenko.portfolio.ws.auth.logic.service;


import com.eustimenko.portfolio.ws.auth.persistent.entity.*;

import java.util.NoSuchElementException;

public interface UserService {

    User getUserByEmail(String email) throws NoSuchElementException;

    Token getActualUserToken(User user);
}
