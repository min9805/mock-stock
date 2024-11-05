package com.mock.investment.stock.domain.stock.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.dto.StockCodeRequest;
import com.mock.investment.stock.domain.stock.exception.InvalidStockCodeRequestException;
import com.mock.investment.stock.domain.stock.exception.UpbitAPIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
	private final StockRepository stockRepository;
	private final HttpClient httpClient;

	public CompletableFuture<List<Stock>> updateStocksFromUpbitAsync() {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.upbit.com/v1/market/all"))
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
						return objectMapper.readValue(body,
								new TypeReference<List<StockCodeRequest>>() {});
					} catch (JsonProcessingException e) {
						throw new InvalidStockCodeRequestException(e.getMessage());
					}
				})
				.thenApply(this::updateStocks);
	}

	@Transactional
	protected List<Stock> updateStocks(List<StockCodeRequest> stockCodeRequests) {
		Set<String> existingMarkets = stockRepository.findAll().stream()
				.map(Stock::getCode)
				.collect(Collectors.toSet());

		List<Stock> newStocks = stockCodeRequests.stream()
				.filter(request -> !existingMarkets.contains(request.getMarket()))
				.map(StockCodeRequest::toEntity)
				.collect(Collectors.toList());

		return stockRepository.saveAll(newStocks);
	}

}