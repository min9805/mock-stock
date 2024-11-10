package com.mock.investment.stock.domain.account.dto;

import com.mock.investment.stock.domain.account.domain.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SimpleAccountDto {
	@NotBlank
	private String accountNumber;

	@PositiveOrZero
	private BigDecimal krwBalance;

	@PositiveOrZero
	private BigDecimal usdBalance;

	@PositiveOrZero
	private BigDecimal bitcoinBalance;

	static public SimpleAccountDto fromEntity(Account account) {
		return SimpleAccountDto.builder()
				.accountNumber(account.getAccountNumber())
				.krwBalance(account.getKrwBalance())
				.usdBalance(account.getUsdBalance())
				.bitcoinBalance(account.getBitcoinBalance())
				.build();
	}

	static public List<SimpleAccountDto> fromEntities(List<Account> accounts) {
		return accounts.stream()
				.map(SimpleAccountDto::fromEntity)
				.toList();
	}
}