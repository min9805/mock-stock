package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoldingStockService {
    private final HoldingStockRepository holdingStockRepository;

    /**
     * 보유 주식 조회
     */
    public void getHoldingStocks(String accountNumber) {
        holdingStockRepository.findByAccount_AccountNumber(accountNumber);
    }
}
