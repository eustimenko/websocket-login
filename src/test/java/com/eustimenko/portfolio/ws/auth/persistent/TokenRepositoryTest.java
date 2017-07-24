package com.eustimenko.portfolio.ws.auth.persistent;

import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import com.eustimenko.portfolio.ws.auth.persistent.repository.TokenRepository;
import org.junit.*;
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

    @After
    public void tearDown() {
        repository.deleteAllInBatch();
    }

    @Test
    public void getTokenByExistingUser() throws Exception {
        final User user = prepareUser();
        final Token expected = prepareToken(user);

        final Token actual = repository.getTokenByUser(user.getEmail()).get();

        assertEquals(expected, actual);
    }

    private User prepareUser() {
        return em.persist(new User("admin@admin.com", "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu"));
    }

    private Token prepareToken(User user) {
        final String token = "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu";
        return em.persist(new Token(token, user));
    }

    @Test
    public void getTokenByNonExistingUser() throws Exception {
        assertFalse(repository.getTokenByUser("user@admin.com").isPresent());
    }
}