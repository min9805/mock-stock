package com.mock.investment.stock.domain.stock.api;

import com.mock.investment.stock.domain.stock.application.StockService;
import com.mock.investment.stock.domain.stock.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@Tag(name = "Stock")
public class StockController {
	private final StockService stockService;
	private final RedissonClient redissonClient;

	@GetMapping("/update")
	public List<StockDto> updateStocksFromUpbitAsync() throws ExecutionException, InterruptedException, TimeoutException {
		return stockService.updateStocksFromBybitAsync().get(5, TimeUnit.SECONDS);
	}

	@GetMapping("/price/{symbol}")
	public BigDecimal getStockPrice(String symbol) {
		return stockService.getStockPrice(symbol);
	}

	@GetMapping("/prices")
	public ConcurrentHashMap<String, BigDecimal> getCurrentPrices() {
		return stockService.getCurrentPrices();
	}

	@GetMapping("/list/{quoteCoin}")
	public StockTickPageResponse<StockTickDto> getStockList(
			@PathVariable("quoteCoin") String quoteCoin,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		String quote = quoteCoin.equals("All") ? "" : quoteCoin;
		return stockService.getQuoteStocksOrderByTurnover(quote, page, size);
	}

	@GetMapping("/kline")
	public KlineDto getKlineData(
			@RequestParam(defaultValue = "BTCUSDT") String symbol,
			@RequestParam(defaultValue = "1") Integer interval,
			@RequestParam(defaultValue = "m") Character unit,
			@RequestParam(defaultValue = "1735689600000") long endTime,
			@RequestParam(defaultValue = "30") int limit) {
		return stockService.getKlineData(symbol, interval, unit, endTime, limit);
	}

	@MessageMapping("/send")
	public void getStockPriceByWebsocket(String symbol) {
		RTopic topic = redissonClient.getTopic("stock");
		topic.publish(symbol);
	}
}