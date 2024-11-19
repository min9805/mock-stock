package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.domain.HoldingStock;
import com.mock.investment.stock.domain.order.dao.SellOrderRepository;
import com.mock.investment.stock.domain.order.domain.SellOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.dto.*;
import com.mock.investment.stock.domain.order.exception.InvalidOrderToSellException;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.global.websocket.StockInfoHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellOrderServiceImpl implements OrderService<SellOrderRequest, SellOrderDto> {
    private final SellOrderRepository SellOrderRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    private final StockInfoHolder stockInfoHolder;
    private final HoldingStockRepository holdingStockRepository;

    /**
     * 매수 주문 조회
     */
    @Override
    public List<SellOrderDto> getOrders(OrderRequest orderRequest){
        List<SellOrder> SellOrders = SellOrderRepository.findByAccount_AccountNumberAndStockSymbol(orderRequest.getAccountNumber(), orderRequest.getSymbol());

        return SellOrders.stream()
                .map(SellOrderDto::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     * 주문 생성 - 시장가 주문
     */
    @Transactional
    @Override
    public SellOrderDto createMarketOrder(SellOrderRequest SellOrderRequest){
        BigDecimal currentPrice = stockInfoHolder.getCurrentPrice(SellOrderRequest.getSymbol());

        Account account = accountRepository.findByAccountNumberWithLock(SellOrderRequest.getAccountNumber());

        holdingStockRepository.findFirstByAccount_AccountNumberAndStockSymbol(SellOrderRequest.getAccountNumber(), SellOrderRequest.getSymbol())
                .ifPresentOrElse(
                        holdingStock -> updateExistingHoldingStock((HoldingStock) holdingStock, SellOrderRequest),
                        () -> { throw new InvalidOrderToSellException(SellOrderRequest.getSymbol()); }
                );

        SellOrder order = SellOrder.builder()
                .stock(stockRepository.getReferenceById(SellOrderRequest.getSymbol()))
                .account(account)
                .orderType(SellOrderRequest.getOrderType())
                .price(currentPrice)
                .quantity(SellOrderRequest.getQuantity())

                .orderStatus(OrderStatus.COMPLETED)
                .filledQuantity(SellOrderRequest.getQuantity())
                .remainingQuantity(BigDecimal.valueOf(0.0))
                .build();

        // 매도 수수료 처리
        BigDecimal sellFee = order.calculateFee();

        account.SellByUSD(
                currentPrice.multiply(SellOrderRequest.getQuantity())
                        .subtract(sellFee)
        );

        accountRepository.save(account);
        SellOrder saveOrder = SellOrderRepository.save(order);

        return SellOrderDto.fromEntity(saveOrder);
    }


    private void updateExistingHoldingStock(HoldingStock holdingStock, SellOrderRequest SellOrderRequest) {
        if (holdingStock.getQuantity().compareTo(SellOrderRequest.getQuantity()) < 0) {
            throw new InvalidOrderToSellException("Trying to sell more than holding quantity");
        }

        holdingStock.subtractQuantity(SellOrderRequest.getQuantity());
        holdingStockRepository.save(holdingStock);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long SellOrderId){
        SellOrder SellOrder = SellOrderRepository.findById(SellOrderId).orElseThrow();

        //주문 취소 - 실패 시 예외 발생
        SellOrder.cancel();
        SellOrderRepository.save(SellOrder);

        // TODO 주문 취소 시스템에 적용
    }


    /**
     * 주문 수정 - 주문 취소 후 새로운 주문 생성
     */
    @Transactional
    @Override
    public void modifyOrder(OrderModifyRequest orderModifyRequest){
        SellOrder SellOrder = SellOrderRepository.findById(orderModifyRequest.getOrderId()).orElseThrow();

        //주문 취소 - 실패 시 예외 발생
        SellOrder.cancel();

        //새로운 주문 생성
        SellOrder newOrder = SellOrder.createModifiedOrder(SellOrder, orderModifyRequest.getPrice());

        SellOrderRepository.save(SellOrder);
        SellOrderRepository.save(newOrder);
    }
}
