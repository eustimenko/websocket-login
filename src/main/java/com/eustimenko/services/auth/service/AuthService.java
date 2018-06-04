package com.eustimenko.services.auth.service;

import com.eustimenko.services.auth.message.*;

public interface AuthService {

    Message auth(LoginMessage data);
}
