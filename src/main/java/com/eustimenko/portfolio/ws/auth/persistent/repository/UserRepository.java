package com.eustimenko.portfolio.ws.auth.persistent.repository;

import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends SimpleJpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
