package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findByUserId(Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
	Account findByAccountNumberWithLock(String accountNumber);

	Account findByAccountNumber(String accountNumber);
}