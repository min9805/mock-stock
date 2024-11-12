package com.mock.investment.stock.domain.account.api;

import com.mock.investment.stock.domain.account.application.WatchStockService;
import com.mock.investment.stock.domain.account.domain.WatchStock;
import com.mock.investment.stock.domain.account.dto.WatchStockRequest;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.global.jwt.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/watch")
@RequiredArgsConstructor
public class WatchStockController {
    private final WatchStockService watchStockService;

    /**
     * 내 관심 종목 조회
     */
    @GetMapping("/")
    public List<StockDto> getWatchStocks(
            @CurrentUser User user
    ) {
        return watchStockService.getWatchStocks(user);
    }

    /**
     * 관심 종목 설정
     */
    @PostMapping("/")
    public void setWatchStocks(
            @CurrentUser User user,
            @RequestBody WatchStockRequest watchStockRequest
    ) {
        watchStockService.setWatchStocks(user, watchStockRequest);
    }

    /**
     * 관심 종목 삭제
     */
    @DeleteMapping("/")
    public void deleteWatchStocks(
            @CurrentUser User user,
            @RequestBody WatchStockRequest watchStockRequest
    ) {
        watchStockService.deleteWatchStocks(user, watchStockRequest);
    }
}
