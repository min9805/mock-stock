package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}