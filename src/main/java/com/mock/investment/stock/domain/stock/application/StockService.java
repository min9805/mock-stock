package com.mock.investment.stock.domain.stock.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.investment.stock.domain.stock.dao.*;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.dto.*;
import com.mock.investment.stock.domain.stock.exception.InvalidStockCodeRequestException;
import com.mock.investment.stock.domain.stock.exception.UpbitAPIException;
import com.mock.investment.stock.global.websocket.StockInfoHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
	private final StockRepository stockRepository;
	private final StockBulkRepository stockBulkRepository;
	private final StockTickRepositoryImpl stockTickRepositoryImpl;
	private final StockTickRepository stockTickRepository;
	private final CandleRepository candleRepository;

	private final StockInfoHolder stockInfoHolder;
	private final HttpClient httpClient;

	private final RedissonClient redissonClient;
	private final ObjectMapper objectMapper;

	@PostConstruct
	@Transactional
	public void init() {
		updateStocksFromBybitAsync();
	}

	public CompletableFuture<List<StockDto>> updateStocksFromBybitAsync() {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.bybit.com/v5/market/instruments-info?category=spot&limit=500"))
				.header("Accept", "application/json")
				.GET()
				.build();

		return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(response -> {
					if (response.statusCode() != 200) {
						throw new UpbitAPIException(response.body());
					}
					return response.body();
				})
				.thenApply(body -> {
					try {
						ObjectMapper objectMapper = new ObjectMapper();
						StockCodeRequestFromBybit response = objectMapper.readValue(
								body,
								StockCodeRequestFromBybit.class
						);
						return response.getResult().getList();
					} catch (JsonProcessingException e) {
						throw new InvalidStockCodeRequestException(e.getMessage());
					}
				})
				.thenApply(this::updateStocks);
	}

	@Transactional
	protected List<StockDto> updateStocks(List<StockCodeRequestFromBybit.StockRequest> stockCodeRequestFromUpbits) {
		Set<String> existingMarkets = stockRepository.findAll().stream()
				.map(Stock::getSymbol)
				.collect(Collectors.toSet());

		List<Stock> newStocks = stockCodeRequestFromUpbits.stream()
				.filter(request -> !existingMarkets.contains(request.getSymbol()))
				.map(StockCodeRequestFromBybit.StockRequest::toEntity)
				.collect(Collectors.toList());

		return stockBulkRepository.saveAll(newStocks);
	}

    public BigDecimal getStockPrice(String symbol) {
		return stockInfoHolder.getCurrentPrice(symbol);
    }

	public ConcurrentHashMap<String, BigDecimal> getCurrentPrices() {
		return stockInfoHolder.getCurrentPrices();
	}

	/**
	 * 주어진 baseCoin에 해당하는 Stock 목록을 반환한다.
	 */
	public List<StockDto> getStockList(String quoteCoin) {
		Collection<Stock> stocks = stockRepository.findByQuoteCoin(quoteCoin);
		return stocks.stream()
				.map(StockDto::fromEntity)
				.collect(Collectors.toList());
	}

	public StockTickPageResponse<StockTickDto> getQuoteStocksOrderByTurnover(String quoteCoin, int page, int size) {
		Pageable pageable = Pageable.ofSize(size).withPage(page);
		return stockTickRepositoryImpl.findLatestQuoteStocksOrderByTurnover(quoteCoin, pageable);
	}

	public KlineDto getKlineData(String symbol, int interval, Character unit, long endTime, int limit) {
		RDeque<String> deque = redissonClient.getDeque("candle:" + symbol + ":" + interval + unit);
		List<CandleStick> redisCandles = deque.stream()
				.map(this::parseCandleData)
				.filter(candle -> candle.getStartTime() <= endTime)
				.collect(Collectors.toList());

		if (redisCandles.size() >= limit) {
			return KlineDto.builder()
					.symbol(symbol)
					.list(redisCandles.subList(0, limit))
					.build();
		}

		List<CandleStick> mongoCandles = candleRepository.findCandlesBeforeEndTime(symbol, interval, unit, endTime, limit - redisCandles.size());
		List<CandleStick> candles = List.of(redisCandles, mongoCandles).stream()
				.flatMap(List::stream)
				.sorted((c1, c2) -> Long.compare(c2.getStartTime(), c1.getStartTime()))
				.limit(limit)
				.collect(Collectors.toList());

		return KlineDto.builder()
				.symbol(symbol)
				.list(candles)
				.build();
	}

	private CandleStick parseCandleData(String data) {
		try {
			return objectMapper.readValue(data, CandleStick.class);
		} catch (Exception e) {
			log.error("Error parsing redis data", e);
			return null;
		}
	}
}