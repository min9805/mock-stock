package com.mock.investment.stock.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OAuthProvider {
    LOCAL(0, "local"),
    NAVER(1, "naver"),
    KAKAO(2, "kakao");

    private int code;
    private String status;

    public static OAuthProvider of(int code) {
        for (OAuthProvider oAuthProvider : OAuthProvider.values()) {
            if (oAuthProvider.getCode() == code) {
                return oAuthProvider;
            }
        }
        //TODO
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
