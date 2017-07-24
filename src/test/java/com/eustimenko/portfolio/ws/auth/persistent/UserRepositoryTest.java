package com.eustimenko.portfolio.ws.auth.persistent;

import com.eustimenko.portfolio.ws.auth.persistent.entity.User;
import com.eustimenko.portfolio.ws.auth.persistent.repository.UserRepository;
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
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository repository;

    @After
    public void tearDown() {
        repository.deleteAllInBatch();
    }

    @Test
    public void getExistingUserByEmail() {
        final User expected = prepareUser();
        final User actual = repository.findByEmail("AdmiN@adMin.com").get();

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
    }

    private User prepareUser() {
        return em.persist(new User("admin@admin.com", "$2a$10$dTLh8sSdqkQVpoL31tt9renepDKsFNLKwkJdjEbg5uQV2kli0C2qu"));
    }

    @Test
    public void getNonExistingUserByEmail() {
        assertFalse(repository.findByEmail("user@admin.com").isPresent());
    }
}
