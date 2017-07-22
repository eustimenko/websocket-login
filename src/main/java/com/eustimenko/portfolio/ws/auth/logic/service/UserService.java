package com.eustimenko.portfolio.ws.auth.logic.service;


import com.eustimenko.portfolio.ws.auth.persistent.entity.*;

public interface UserService {

    User getUserByEmail(String email);

    Token getActualUserToken(User user);
}
