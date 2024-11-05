package com.mock.investment.stock.domain.stock.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UpbitAPIException extends BaseException {
	public UpbitAPIException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}