package com.mock.investment.stock.domain.stock.exception;

import com.mock.investment.stock.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidStockCodeRequestException extends BaseException {
	public InvalidStockCodeRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}