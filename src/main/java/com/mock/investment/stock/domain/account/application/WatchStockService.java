package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.WatchStockRepository;
import com.mock.investment.stock.domain.account.domain.WatchStock;
import com.mock.investment.stock.domain.account.dto.WatchStockRequest;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import com.mock.investment.stock.domain.user.dao.UserRepository;
import com.mock.investment.stock.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchStockService {
    private final WatchStockRepository watchStockRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    /**
     * 내 관심 종목 조회
     */
    public List<StockDto> getWatchStocks(User user) {
        List<WatchStock> watchList = watchStockRepository.findByUserId(user.getId());

        return StockDto.fromEntities(
                watchList.stream()
                        .map(WatchStock::getStock)
                        .toList()
        );
    }

    /**
     * 관심 종목 설정
     */
    public void setWatchStocks(User user, WatchStockRequest watchStockRequest) {
        WatchStock watchStock = WatchStock.builder()
                .user(userRepository.getReferenceById(user.getId()))
                .stock(stockRepository.findBySymbol(watchStockRequest.getSymbol()))
                .build();

        watchStockRepository.save(watchStock);
    }

    /**
     * 관심 종목 삭제
     */
    public void deleteWatchStocks(User user, WatchStockRequest watchStockRequest) {
        watchStockRepository.deleteByUserIdAndStockSymbol(user.getId(), watchStockRequest.getSymbol());
    }
}
