package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.domain.HoldingStock;
import com.mock.investment.stock.domain.order.dao.BuyOrderRepository;
import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.dto.*;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.global.websocket.PriceHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyOrderServiceImpl implements OrderService<BuyOrderRequest, BuyOrderDto> {
    private final BuyOrderRepository buyOrderRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    private final PriceHolder priceHolder;
    private final HoldingStockRepository holdingStockRepository;

    /**
     * 매수 주문 조회
     */
    @Override
    public List<BuyOrderDto> getOrders(OrderRequest orderRequest){
        List<BuyOrder> buyOrders = buyOrderRepository.findByAccount_AccountNumberAndStockSymbol(orderRequest.getAccountNumber(), orderRequest.getSymbol());

        return buyOrders.stream()
                .map(BuyOrderDto::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     * 주문 생성 - 시장가 주문
     */
    @Transactional
    @Override
    public BuyOrderDto createMarketOrder(BuyOrderRequest buyOrderRequest){
        BigDecimal currentPrice = priceHolder.getCurrentPrice(buyOrderRequest.getSymbol());

        Account account = accountRepository.findByAccountNumber(buyOrderRequest.getAccountNumber());

        BuyOrder order = BuyOrder.builder()
                .stock(stockRepository.getReferenceById(buyOrderRequest.getSymbol()))
                .account(account)
                .orderType(buyOrderRequest.getOrderType())
                .price(currentPrice)
                .quantity(buyOrderRequest.getQuantity())

                .orderStatus(OrderStatus.COMPLETED)
                .filledQuantity(buyOrderRequest.getQuantity())
                .remainingQuantity(BigDecimal.valueOf(0.0))
                .build();

        account.buyByUSD(currentPrice.multiply(buyOrderRequest.getQuantity()));

        holdingStockRepository.findByAccount_AccountNumberAndStockSymbol(buyOrderRequest.getAccountNumber(), buyOrderRequest.getSymbol())
            .ifPresentOrElse(
                    holdingStock -> updateExistingHoldingStock((HoldingStock) holdingStock, buyOrderRequest, currentPrice),
                    () -> createNewHoldingStock(account, buyOrderRequest, currentPrice)
            );

        accountRepository.save(account);
        BuyOrder saveOrder = buyOrderRepository.save(order);

        return BuyOrderDto.fromEntity(saveOrder);
    }


    private void updateExistingHoldingStock(HoldingStock holdingStock, BuyOrderRequest buyOrderRequest, BigDecimal currentPrice) {
        holdingStock.addQuantity(buyOrderRequest.getQuantity(), currentPrice);
    }

    private void createNewHoldingStock(Account account, BuyOrderRequest buyOrderRequest, BigDecimal currentPrice) {
        HoldingStock newHoldingStock = HoldingStock.builder()
                .account(account)
                .stock(stockRepository.getReferenceById(buyOrderRequest.getSymbol()))
                .quantity(buyOrderRequest.getQuantity())
                .avgPrice(currentPrice.doubleValue())
                .build();
        holdingStockRepository.save(newHoldingStock);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long buyOrderId){
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
    @Override
    public void modifyOrder(OrderModifyRequest orderModifyRequest){
        BuyOrder buyOrder = buyOrderRepository.findById(orderModifyRequest.getOrderId()).orElseThrow();

        //주문 취소 - 실패 시 예외 발생
        buyOrder.cancel();

        //새로운 주문 생성
        BuyOrder newOrder = buyOrder.createModifiedOrder(buyOrder, orderModifyRequest.getPrice());

        buyOrderRepository.save(buyOrder);
        buyOrderRepository.save(newOrder);
    }
}
