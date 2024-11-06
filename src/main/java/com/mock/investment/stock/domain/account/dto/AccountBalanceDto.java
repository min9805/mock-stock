package com.mock.investment.stock.domain.account.dto;

import com.mock.investment.stock.domain.account.domain.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.mock.investment.stock.domain.account.domain.Account}
 */
@Data
@Builder
public class AccountBalanceDto {
	@NotBlank
	String account_number;

	@PositiveOrZero
	Double krw_balance;

	@PositiveOrZero
	Double usd_balance;

	@PositiveOrZero
	Double bitcoin_balance;

	public static AccountBalanceDto fromEntity(Account account) {
		return AccountBalanceDto.builder()
				.account_number(account.getAccount_number())
				.krw_balance(account.getKrw_balance())
				.usd_balance(account.getUsd_balance())
				.bitcoin_balance(account.getBitcoin_balance())
				.build();
	}
}