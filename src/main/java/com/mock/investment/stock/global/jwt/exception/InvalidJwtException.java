package com.mock.investment.stock.global.jwt.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;


public class InvalidJwtException extends BaseException {
    public InvalidJwtException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
