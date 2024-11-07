package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.dto.AccountBalanceDto;
import com.mock.investment.stock.domain.account.dto.SimpleAccountDto;
import com.mock.investment.stock.domain.account.exception.AccountAlreadyExistsException;
import com.mock.investment.stock.domain.account.exception.AccountNotFoundException;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import com.mock.investment.stock.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	public SimpleAccountDto findByUserId(Long userId) {
		List<Account> accout = accountRepository.findByUserId(userId);

		if (accout.isEmpty()) {
			throw new AccountNotFoundException();
		}

		return SimpleAccountDto.fromEntity(accout.get(0));
	}

	public SimpleAccountDto createAccount(Long userId) {
		if (!accountRepository.findByUserId(userId).isEmpty()) {
			throw new AccountAlreadyExistsException();
		}

		Account account = Account.builder()
				.user(userRepository.getReferenceById(userId))
				.krwBalance(0.0)
				.usdBalance(1000000.0)
				.bitcoinBalance(0.0)
				.build();

		accountRepository.save(account);

		return SimpleAccountDto.fromEntity(account);
	}
}