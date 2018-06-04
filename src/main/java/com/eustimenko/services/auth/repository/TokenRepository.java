package com.eustimenko.services.auth.repository;

import com.eustimenko.services.auth.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
}
