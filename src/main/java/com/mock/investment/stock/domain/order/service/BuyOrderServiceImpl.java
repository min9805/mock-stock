package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.domain.HoldingStock;
import com.mock.investment.stock.domain.order.dao.BuyOrderRepository;
import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.dto.*;
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
public class BuyOrderServiceImpl implements OrderService<BuyOrderRequest, BuyOrderDto> {
    private final BuyOrderRepository buyOrderRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    private final StockInfoHolder stockInfoHolder;
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
    @Override
    @Transactional
    public BuyOrderDto createMarketOrder(BuyOrderRequest buyOrderRequest){
        // 현재 주문 금액 조회
        BigDecimal currentPrice = stockInfoHolder.getCurrentPrice(buyOrderRequest.getSymbol());

        // 계좌 조회 및 금액 차감
        Account account = accountRepository.findByAccountNumberWithLock(buyOrderRequest.getAccountNumber());
        account.buyByUSD(currentPrice.multiply(buyOrderRequest.getQuantity()));
        accountRepository.save(account);

        // 주문 생성
        BuyOrder order = BuyOrder.of(buyOrderRequest, stockRepository.getReferenceById(buyOrderRequest.getSymbol()), account, currentPrice);
        BuyOrder saveOrder = buyOrderRepository.save(order);

        // 보유 주식 추가
        holdingStockRepository.findFirstByAccount_AccountNumberAndStockSymbol(buyOrderRequest.getAccountNumber(), buyOrderRequest.getSymbol())
                .ifPresentOrElse(
                        holdingStock -> updateExistingHoldingStock(holdingStock, buyOrderRequest, currentPrice),
                        () -> createNewHoldingStock(account, buyOrderRequest, currentPrice)
                );

        return BuyOrderDto.fromEntity(saveOrder);
    }


    private void updateExistingHoldingStock(HoldingStock holdingStock, BuyOrderRequest buyOrderRequest, BigDecimal currentPrice) {
        holdingStock.addQuantity(buyOrderRequest.getQuantity(), currentPrice);
        holdingStockRepository.save(holdingStock);
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
