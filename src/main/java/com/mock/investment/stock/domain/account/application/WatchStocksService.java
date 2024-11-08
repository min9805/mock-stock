package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.WatchStockRepository;
import com.mock.investment.stock.domain.account.domain.WatchStock;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import com.mock.investment.stock.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchStocksService {
    private final WatchStockRepository watchStockRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    /**
     * 내 관심 종목 조회
     */
    public List<StockDto> getWatchStocks(Long userId) {
        List<WatchStock> watchList = watchStockRepository.findByUserId(userId);

        return StockDto.fromEntities(
                watchList.stream()
                        .map(WatchStock::getStock)
                        .toList()
        );
    }

    /**
     * 관심 종목 설정
     */
    public void setWatchStocks(Long userId, String coinSymbol) {
        WatchStock watchStock = WatchStock.builder()
                .user(userRepository.getReferenceById(userId))
                .stock(stockRepository.findBySymbol(coinSymbol))
                .build();
    }

    /**
     * 관심 종목 삭제
     */
    public void deleteWatchStocks(Long userId, String coinSymbol) {
        watchStockRepository.deleteByUserIdAndStockSymbol(userId, coinSymbol);
    }
}
