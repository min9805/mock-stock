package com.mock.investment.stock.domain.user.exception;

import org.springframework.http.HttpStatus;
import com.mock.investment.stock.global.exception.BaseException;

public class UserCreateValidationException extends BaseException {
    public UserCreateValidationException() {
        super(HttpStatus.CONFLICT, "가입 시 중복된 정보가 있습니다.");
    }
}
