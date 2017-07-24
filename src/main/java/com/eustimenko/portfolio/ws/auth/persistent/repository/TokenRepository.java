package com.eustimenko.portfolio.ws.auth.persistent.repository;

import com.eustimenko.portfolio.ws.auth.persistent.entity.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends SimpleJpaRepository<Token, String> {

    @Query("SELECT t from Token t WHERE t.user.email=:email AND t.expiredDate = (SELECT MAX(tt.expiredDate) FROM Token tt WHERE tt.user.id=t.user.id)")
    Optional<Token> getTokenByUser(@Param("email") String email);
}
