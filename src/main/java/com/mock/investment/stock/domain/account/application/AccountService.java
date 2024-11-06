package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.dto.AccountBalanceDto;
import com.mock.investment.stock.domain.account.dto.SimpleAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;

	public List<SimpleAccountDto> findByUserId(Long userId) {
		List<Account> accouts = accountRepository.findByUserId(userId);
		return SimpleAccountDto.fromEntities(accouts);
	}

	public AccountBalanceDto getAccountBalance(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber);
		return AccountBalanceDto.fromEntity(account);
	}
}