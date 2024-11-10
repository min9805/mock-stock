package com.mock.investment.stock.domain.order.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidOrderToSellException extends BaseException {
    public InvalidOrderToSellException(String message) {
        super(HttpStatus.BAD_REQUEST, "Invalid order to sell: " + message);
    }
}
