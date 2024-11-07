package com.mock.investment.stock.domain.account.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Account Not Found");
    }
}
