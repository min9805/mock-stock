package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.dto.AccountBalanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;

	public AccountBalanceDto getAccountBalance(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber);
		return AccountBalanceDto.fromEntity(account);
	}
}