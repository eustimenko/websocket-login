package com.eustimenko.portfolio.ws.auth.persistent;

import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import com.eustimenko.portfolio.ws.auth.persistent.repository.UserRepository;
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
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository repository;

    @Test
    public void getExistingUserByEmail() {
        final String email = "admin@admin.com";
        final User user = new User(email, "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu");
        em.persist(user);

        final User actual = repository.findByEmail(email);
        assertNotNull(actual);
        assertEquals(user.getId(), actual.getId());
    }

    @Test
    public void getNonExistingUserByEmail() {
        em.persist(new User("admin@admin.com", "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu"));
        assertNull(repository.findByEmail("user@admin.com"));
    }
}
