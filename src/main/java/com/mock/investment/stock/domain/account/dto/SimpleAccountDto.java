package com.mock.investment.stock.domain.account.dto;

import com.mock.investment.stock.domain.account.domain.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SimpleAccountDto {
	@NotBlank
	private String account_number;

	@PositiveOrZero
	private Double krw_balance;

	@PositiveOrZero
	private Double usd_balance;

	@PositiveOrZero
	private Double bitcoin_balance;

	static public SimpleAccountDto fromEntity(Account account) {
		return SimpleAccountDto.builder()
				.account_number(account.getAccount_number())
				.krw_balance(account.getKrw_balance())
				.usd_balance(account.getUsd_balance())
				.bitcoin_balance(account.getBitcoin_balance())
				.build();
	}

	static public List<SimpleAccountDto> fromEntities(List<Account> accounts) {
		return accounts.stream()
				.map(SimpleAccountDto::fromEntity)
				.toList();
	}
}