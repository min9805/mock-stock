package com.mock.investment.stock.domain.account.domain;

import com.mock.investment.stock.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

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
	private Long accountId;

	@Column(unique = true)
	private String accountNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private Double krwBalance;

	private Double usdBalance;

	private Double bitcoinBalance;

	public void createAccount() {
		this.accountNumber = generatedAccountNumber();
		this.krwBalance = 0.0;
		this.usdBalance = 1000000.0;
		this.bitcoinBalance = 0.0;
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
}