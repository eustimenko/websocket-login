package com.eustimenko.portfolio.ws.auth.persistent;

import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import com.eustimenko.portfolio.ws.auth.persistent.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
public class TokenRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TokenRepository repository;

    @Test
    public void getTokenByExistingUser() throws Exception {
        final User user = new User("admin@admin.com", "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu");
        em.persist(user);
        final String token = "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu";
        final Token savedToken = em.persist(new Token(token, user));

        final Token actualToken = repository.getTokenByUser(user.getEmail());
        assertEquals(savedToken, actualToken);
    }

    @Test
    public void getTokenByNonExistingUser() throws Exception {
        User user = new User("admin@admin.com", "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu");
        em.persist(user);
        final String token = "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu";
        final Token savedToken = em.persist(new Token(token, user));

        user = new User("user@admin.com", "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu");

        final Token actualToken = repository.getTokenByUser(user.getEmail());
        assertNotEquals(savedToken, actualToken);
    }
}