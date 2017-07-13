package com.eustimenko.portfolio.ws.auth.persistent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Token implements Serializable {

    private static final long EXPIRATION_MINUTES = 15;

    @Id
    @Column(name = "token", nullable = false)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expiredDate = LocalDateTime.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Transient
    public boolean isExpired() {
        return expiredDate.isBefore(LocalDateTime.now());
    }
}
