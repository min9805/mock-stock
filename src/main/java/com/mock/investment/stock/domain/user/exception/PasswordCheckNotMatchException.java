package com.mock.investment.stock.domain.user.exception;

import org.springframework.http.HttpStatus;
import com.mock.investment.stock.global.exception.BaseException;

public class PasswordCheckNotMatchException extends BaseException {
    public PasswordCheckNotMatchException() {
        super(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
    }
}
