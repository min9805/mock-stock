package com.mock.investment.stock.domain.order.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidOrderToCancelException extends BaseException {
    public InvalidOrderToCancelException(long orderId) {
        super(HttpStatus.BAD_REQUEST, "Invalid order to cancel: " + orderId);
    }
}
