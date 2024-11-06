package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.order.dao.BuyOrderRepository;
import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderModifyRequest;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.dto.OrderRequest;
import com.mock.investment.stock.domain.order.exception.InvalidOrderToCancelException;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyOrderService {
    private final BuyOrderRepository buyOrderRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    /**
     * 매수 주문 조회
     */
    public List<BuyOrderDto> getBuyOrders(OrderRequest orderRequest){
        List<BuyOrder> buyOrders = buyOrderRepository.findByAccount_AccountNumberAndStockCode(orderRequest.getAccountNumber(), orderRequest.getStockCode());

        return buyOrders.stream()
                .map(BuyOrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 주문 생성
     */
    @Transactional
    public void createBuyOrder(BuyOrderRequest buyOrderRequest){
        BuyOrder order = BuyOrder.builder()
                .stock(stockRepository.getReferenceById(buyOrderRequest.getCode()))
                .account(accountRepository.findByAccountNumber(buyOrderRequest.getAccountNumber()))
                .orderType(buyOrderRequest.getOrderType())
                .buyPrice(buyOrderRequest.getPrice())
                .buyQuantity(buyOrderRequest.getQuantity())

                .orderStatus(OrderStatus.PENDING)
                .filledQuantity(0)
                .remainingQuantity(buyOrderRequest.getQuantity())
                .build();

        buyOrderRepository.save(order);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelBuyOrder(Long buyOrderId){
        BuyOrder buyOrder = buyOrderRepository.findById(buyOrderId).orElseThrow();

        //주문 취소 - 실패 시 예외 발생
        buyOrder.cancel();
        buyOrderRepository.save(buyOrder);

        // TODO 주문 취소 시스템에 적용
    }

    /**
     * 주문 수정 - 주문 취소 후 새로운 주문 생성
     */
    @Transactional
    public void modifyBuyOrder(BuyOrderModifyRequest buyOrderRequest){
        BuyOrder buyOrder = buyOrderRepository.findById(buyOrderRequest.getOrderId()).orElseThrow();

        //주문 취소 - 실패 시 예외 발생
        buyOrder.cancel();

        //새로운 주문 생성
        BuyOrder newOrder = buyOrder.createModifiedOrder(buyOrder, buyOrderRequest.getPrice());

        buyOrderRepository.save(buyOrder);
        buyOrderRepository.save(newOrder);
    }
}
