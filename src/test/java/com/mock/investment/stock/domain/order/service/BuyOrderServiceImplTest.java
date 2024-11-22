package com.mock.investment.stock.domain.order.service;

import com.mock.investment.stock.domain.account.dao.AccountRepository;
import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.account.domain.HoldingStock;
import com.mock.investment.stock.domain.order.domain.OrderType;
import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.global.websocket.StockInfoHolder;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Slf4j
class BuyOrderServiceConcurrencyTest {

    @Autowired
    private BuyOrderServiceImpl buyOrderService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HoldingStockRepository holdingStockRepository;

    @Autowired
    private StockRepository stockRepository;

    @MockBean
    private StockInfoHolder stockInfoHolder;

    private static final String ACCOUNT_NUMBER = "TEST-ACC-001";
    private static final String STOCK_SYMBOL = "BTCUSDT";
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(5000);
    private static final BigDecimal STOCK_PRICE = BigDecimal.valueOf(100.00);
    private static final int CONCURRENT_USERS = 30;
    private static final BigDecimal ORDER_QUANTITY = BigDecimal.ONE;
    @Autowired
    private HikariDataSource dataSource;

    @BeforeEach
    @Transactional
    void setUp() {
        // 테스트 계좌 생성
        Account testAccount = Account.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .usdBalance(INITIAL_BALANCE)
                .build();
        accountRepository.save(testAccount);

        // StockInfoHolder Mock 설정 (만약 Mock 사용시)
        when(stockInfoHolder.getCurrentPrice(STOCK_SYMBOL))
                .thenReturn(STOCK_PRICE);
    }

    @Test
    void testConcurrentMarketOrders() throws InterruptedException {
        // Given
        int numberOfThreads = CONCURRENT_USERS;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads);
        List<Future<BuyOrderDto>> futures = new ArrayList<>();

        // When: 여러 스레드가 동시에 주문 생성
        for (int i = 0; i < numberOfThreads; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    barrier.await(); // 모든 스레드가 동시에 시작하도록 대기

                    BuyOrderRequest request = BuyOrderRequest.builder()
                            .accountNumber(ACCOUNT_NUMBER)
                            .symbol(STOCK_SYMBOL)
                            .quantity(ORDER_QUANTITY)
                            .orderType(OrderType.MARKET)
                            .build();

                    return buyOrderService.createMarketOrder(request);
                } catch (Exception e) {
                    // 기존 로깅 유지
                    log.error("Error occurred during order creation", e);

                    // 데드락 상세 정보 조회 추가
                    try (Connection conn = dataSource.getConnection();
                         Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery("SHOW ENGINE INNODB STATUS")) {
                        if (rs.next()) {
                            log.error("InnoDB Status at time of deadlock:\n{}", rs.getString("Status"));
                        }
                    } catch (SQLException ex) {
                        log.error("Failed to get InnoDB status", ex);
                    }
                    return null;
                } finally {
                    latch.countDown();
                }
            }));
        }

        // 모든 스레드가 완료될 때까지 대기
        latch.await();
        executorService.shutdown();

        // Then
        // 1. 모든 주문이 성공적으로 처리되었는지 확인
        List<BuyOrderDto> completedOrders = futures.stream()
                .map(this::getFutureResult)
                .filter(order -> order != null)
                .toList();
        assertThat(completedOrders).hasSize(numberOfThreads);

        // 2. 계좌 잔액이 정확한지 확인
        Account finalAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        BigDecimal expectedBalance = INITIAL_BALANCE.subtract(
                STOCK_PRICE.multiply(ORDER_QUANTITY.multiply(BigDecimal.valueOf(numberOfThreads)))
        );
        assertThat(finalAccount.getUsdBalance()).isEqualByComparingTo(expectedBalance);

        // 3. 보유 주식 수량이 정확한지 확인
        HoldingStock holdingStock = holdingStockRepository
                .findFirstByAccount_AccountNumberAndStockSymbol(ACCOUNT_NUMBER, STOCK_SYMBOL)
                .orElseThrow();
        BigDecimal expectedQuantity = ORDER_QUANTITY.multiply(BigDecimal.valueOf(numberOfThreads));
        assertThat(holdingStock.getQuantity()).isEqualByComparingTo(expectedQuantity);
    }

    private BuyOrderDto getFutureResult(Future<BuyOrderDto> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            return null;
        }
    }
}