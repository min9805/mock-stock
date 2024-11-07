package com.mock.investment.stock.domain.account.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AccountAlreadyExistsException extends BaseException {
    public AccountAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "Account already exists");
    }
}
