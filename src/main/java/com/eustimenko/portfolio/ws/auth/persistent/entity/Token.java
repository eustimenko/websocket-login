package com.eustimenko.portfolio.ws.auth.persistent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Token implements Serializable {

    private static final long EXPIRATION_MINUTES = 15;

    @Id
    @Column(nullable = false)
    private String value;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expiredDate = LocalDateTime.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);

    public Token() {
    }

    public Token(String value, User user) {
        this.value = value;
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String toString() {
        return value + ":" + expiredDate;
    }
}
