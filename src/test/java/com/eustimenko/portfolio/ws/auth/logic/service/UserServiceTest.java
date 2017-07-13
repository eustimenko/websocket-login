package com.eustimenko.portfolio.ws.auth.logic.service;

import com.eustimenko.portfolio.ws.auth.persistent.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserService service;

    @Test
    public void getValidUserToken() throws Exception {
        User user = new User("admin@admin.com", "test");
        given(service.getUserByEmail("admin@admin.com")).willReturn(user);

        final Token token = new Token("test", user);
        given(service.getActualUserToken(user)).willReturn(token);

        user = service.getUserByEmail("admin@admin.com");
        service.getActualUserToken(user);

        assertThat(token.getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test(expected = NoSuchElementException.class)
    public void noSuchUser() {
        given(service.getUserByEmail("nonvalid@admin.com")).willThrow(NoSuchElementException.class);
        service.getUserByEmail("nonvalid@admin.com");
    }

    @Test
    public void getByEmail() throws Exception {
        final String pwd = "test";
        User user = new User("admin@admin.com", pwd);
        given(service.getUserByEmail("admin@admin.com")).willReturn(user);

        user = service.getUserByEmail("admin@admin.com");
        assertThat(user.getPassword()).isEqualTo(pwd);
    }
}
