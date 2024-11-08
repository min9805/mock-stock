package com.mock.investment.stock.domain.stock.domain;

import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "stocks")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {
	@Id
	private Long id;

	@Column(unique = true)
	private String symbol;

	private String baseCoin;

	private String quoteCoin;
}