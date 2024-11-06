package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.dto.AccountBalanceDto;
import com.mock.investment.stock.domain.account.dto.SimpleAccountDto;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;

	public SimpleAccountDto findByUserId(Long userId) {
		Account accout = accountRepository.findByUserId(userId).get(0);
		return SimpleAccountDto.fromEntity(accout);
	}
}