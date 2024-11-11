package com.mock.investment.stock.domain.stock.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.investment.stock.domain.stock.dao.StockBulkRepository;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.dto.BybitResponse;
import com.mock.investment.stock.domain.stock.dto.StockCodeRequestFromBybit;
import com.mock.investment.stock.domain.stock.dto.StockCodeRequestFromUpbit;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import com.mock.investment.stock.domain.stock.exception.InvalidStockCodeRequestException;
import com.mock.investment.stock.domain.stock.exception.UpbitAPIException;
import com.mock.investment.stock.global.websocket.PriceHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	private final PriceHolder priceHolder;
	private final HttpClient httpClient;

	@PostConstruct
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
		return priceHolder.getCurrentPrice(symbol);
    }

	public ConcurrentHashMap<String, BigDecimal> getCurrentPrices() {
		return priceHolder.getCurrentPrices();
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
}