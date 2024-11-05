package com.mock.investment.stock.domain.user.exception;

import org.springframework.http.HttpStatus;
import com.mock.investment.stock.global.exception.BaseException;

public class InvalidRefreshTokenException extends BaseException {
    public InvalidRefreshTokenException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다.");
    }
}
