package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.domain.HoldingStock;
import com.mock.investment.stock.domain.order.dao.BuyOrderRepository;
import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderModifyRequest;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.dto.OrderRequest;
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
public class BuyOrderService {
    private final BuyOrderRepository buyOrderRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    private final PriceHolder priceHolder;
    private final HoldingStockRepository holdingStockRepository;

    /**
     * 매수 주문 조회
     */
    public List<BuyOrderDto> getBuyOrders(OrderRequest orderRequest){
        List<BuyOrder> buyOrders = buyOrderRepository.findByAccount_AccountNumberAndStockSymbol(orderRequest.getAccountNumber(), orderRequest.getCoinSymbol());

        return buyOrders.stream()
                .map(BuyOrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 주문 생성 - 시장가 주문
     */
    @Transactional
    public BuyOrderDto createMarketBuyOrder(BuyOrderRequest buyOrderRequest){
        BigDecimal currentPrice = priceHolder.getCurrentPrice(buyOrderRequest.getSymbol());

        Account account = accountRepository.findByAccountNumber(buyOrderRequest.getAccountNumber());

        BuyOrder order = BuyOrder.builder()
                .stock(stockRepository.getReferenceById(buyOrderRequest.getSymbol()))
                .account(account)
                .orderType(buyOrderRequest.getOrderType())
                .price(currentPrice.doubleValue())
                .quantity(buyOrderRequest.getQuantity())

                .orderStatus(OrderStatus.COMPLETED)
                .filledQuantity(buyOrderRequest.getQuantity())
                .remainingQuantity(0.0)
                .build();

        account.buyByUSD(currentPrice.multiply(BigDecimal.valueOf(buyOrderRequest.getQuantity())));

        holdingStockRepository.findByAccount_AccountNumberAndStockSymbol(buyOrderRequest.getAccountNumber(), buyOrderRequest.getSymbol())
            .ifPresentOrElse(
                    holdingStock -> updateExistingHoldingStock((HoldingStock) holdingStock, buyOrderRequest),
                    () -> createNewHoldingStock(account, buyOrderRequest)
            );

        accountRepository.save(account);
        BuyOrder saveOrder = buyOrderRepository.save(order);

        return BuyOrderDto.fromEntity(saveOrder);
    }


    private void updateExistingHoldingStock(HoldingStock holdingStock, BuyOrderRequest buyOrderRequest) {
        holdingStock.addQuantity(buyOrderRequest.getQuantity());
    }

    private void createNewHoldingStock(Account account, BuyOrderRequest buyOrderRequest) {
        HoldingStock newHoldingStock = HoldingStock.builder()
                .account(account)
                .stock(stockRepository.getReferenceById(buyOrderRequest.getSymbol()))
                .quantity(buyOrderRequest.getQuantity())
                .build();
        holdingStockRepository.save(newHoldingStock);
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
