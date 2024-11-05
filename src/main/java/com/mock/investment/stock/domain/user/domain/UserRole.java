package com.mock.investment.stock.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ADMIN(0, "ROLE_ADMIN"),
    USER(1, "ROLE_USER")
    ;

    private int code;
    private String status;

    public static UserRole of(int code) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getCode() == code) {
                return userRole;
            }
        }
        //TODO
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
