package com.mock.investment.stock.global.jwt.refreshtoken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String email;

    protected RefreshToken() {
    }

    public RefreshToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

}
