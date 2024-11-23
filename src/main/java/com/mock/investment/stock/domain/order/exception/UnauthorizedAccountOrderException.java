package com.mock.investment.stock.domain.order.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedAccountOrderException extends BaseException {
	public UnauthorizedAccountOrderException() {
		super(HttpStatus.FORBIDDEN, "계좌로 주문할 권한이 없습니다.");
	}

}