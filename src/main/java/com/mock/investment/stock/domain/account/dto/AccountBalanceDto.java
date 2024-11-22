package com.mock.investment.stock.domain.account.dto;

import com.mock.investment.stock.domain.account.domain.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for {@link com.mock.investment.stock.domain.account.domain.Account}
 */
@Data
@Builder
public class AccountBalanceDto {
	@NotBlank
	String accountNumber;

	@PositiveOrZero
	BigDecimal krwBalance;

	@PositiveOrZero
	BigDecimal usdBalance;

	@PositiveOrZero
	BigDecimal bitcoinBalance;

	public static AccountBalanceDto fromEntity(Account account) {
		return AccountBalanceDto.builder()
				.accountNumber(account.getAccountNumber())
				.krwBalance(account.getKrwBalance())
				.usdBalance(account.getUsdBalance())
				.bitcoinBalance(account.getBitcoinBalance())
				.build();
	}
}