package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findByUserId(Long userId);

	Account findByAccountNumber(String accountNumber);
}