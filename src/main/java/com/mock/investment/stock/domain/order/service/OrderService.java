package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.dto.OrderDto;
import com.mock.investment.stock.domain.order.dto.OrderRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService<T extends OrderRequest, R extends OrderDto> {

    /**
     * 주문 조회
     */
    List<R> getOrders(OrderRequest orderRequest);

    /**
     * 시장가 주문 생성
     */
    R createMarketOrder(T orderRequest);

    /**
     * 주문 취소
     */
    void cancelOrder(Long orderId);

    /**
     * 주문 수정
     */
    void modifyOrder(Object modifyRequest);
}