package com.mock.investment.stock.domain.account.domain;

import com.mock.investment.stock.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "accounts")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String accountNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private BigDecimal krwBalance;

	private BigDecimal usdBalance;

	private BigDecimal bitcoinBalance;

	@OneToMany(mappedBy = "account")
	private List<HoldingStock> holdingStocks;

	public void createAccount() {
		this.accountNumber = generatedAccountNumber();
		this.krwBalance = BigDecimal.valueOf(3000000);
		this.usdBalance = BigDecimal.valueOf(10000000);
		this.bitcoinBalance = BigDecimal.valueOf(5);
	}

	// TODO: 계좌 번호 생성 중복 시 처리
	private String generatedAccountNumber() {
		Random random = new Random();

		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%04d", random.nextInt(10000))).append("-");
		sb.append(String.format("%04d", random.nextInt(10000))).append("-");
		sb.append(String.format("%04d", random.nextInt(100)));

		return sb.toString();
	}

	public void buyByUSD(BigDecimal amount) {
		this.usdBalance = this.usdBalance.subtract(amount);
	}

    public void SellByUSD(BigDecimal multiply) {
		this.usdBalance = this.usdBalance.add(multiply);
	}
}