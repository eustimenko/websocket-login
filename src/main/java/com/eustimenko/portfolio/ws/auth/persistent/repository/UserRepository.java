package com.eustimenko.portfolio.ws.auth.persistent.repository;

import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends SimpleJpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email=:email")
    User findByEmail(@Param("email") String email);
}
