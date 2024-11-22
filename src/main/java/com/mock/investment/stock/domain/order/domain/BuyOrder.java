package com.mock.investment.stock.domain.order.domain;

import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.exception.InvalidOrderToCancelException;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "buy_orders")
@Getter
@SuperBuilder
@AllArgsConstructor
public class BuyOrder extends Order {

    public BuyOrder createModifiedOrder(BuyOrder order, BigDecimal newPrice){
        return BuyOrder.builder()
                .stock(order.getStock())
                .account(order.getAccount())
                .orderType(order.getOrderType())
                .price(newPrice)
                .quantity(order.getRemainingQuantity())
                .orderStatus(OrderStatus.PENDING)
                .filledQuantity(BigDecimal.valueOf(0.0))
                .remainingQuantity(order.getRemainingQuantity())
                .build();
    }

    public static BuyOrder of(BuyOrderRequest buyOrderRequest, Stock stock, Account account, BigDecimal currentPrice){
        return BuyOrder.builder()
                .stock(stock)
                .account(account)
                .orderType(buyOrderRequest.getOrderType())
                .price(currentPrice)
                .quantity(buyOrderRequest.getQuantity())

                .orderStatus(OrderStatus.COMPLETED)
                .filledQuantity(buyOrderRequest.getQuantity())
                .remainingQuantity(BigDecimal.valueOf(0.0))
                .build();
    }
}
