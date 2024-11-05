package com.mock.investment.stock.domain.stock.application;

import com.mock.investment.stock.domain.stock.dao.StockRepository;
import com.mock.investment.stock.domain.stock.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockServiceTest {

	@Mock
	private StockRepository stockRepository;

	@Mock
	private HttpClient httpClient;

	@InjectMocks
	private StockService stockService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void updateStocksFromUpbitAsync_returnsStocks_whenApiCallSucceeds() throws Exception {
		HttpResponse<String> httpResponse = mock(HttpResponse.class);
		when(httpResponse.statusCode()).thenReturn(200);
		when(httpResponse.body()).thenReturn("[{\"market\":\"KRW-BTC\",\"korean_name\":\"비트코인\",\"english_name\":\"Bitcoin\"}]");
		when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenReturn(CompletableFuture.completedFuture(httpResponse));

		CompletableFuture<List<Stock>> result = stockService.updateStocksFromUpbitAsync();

		assertNotNull(result);
		assertEquals(1, result.join().size());
	}

	@Test
	void updateStocksFromUpbitAsync_throwsException_whenApiCallFails() throws Exception {
		HttpResponse<String> httpResponse = mock(HttpResponse.class);
		when(httpResponse.statusCode()).thenReturn(500);
		when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenReturn(CompletableFuture.completedFuture(httpResponse));

		CompletableFuture<List<Stock>> result = stockService.updateStocksFromUpbitAsync();

		assertThrows(RuntimeException.class, result::join);
	}

	@Test
	void updateStocksFromUpbitAsync_throwsException_whenJsonProcessingFails() throws Exception {
		HttpResponse<String> httpResponse = mock(HttpResponse.class);
		when(httpResponse.statusCode()).thenReturn(200);
		when(httpResponse.body()).thenReturn("invalid json");
		when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenReturn(CompletableFuture.completedFuture(httpResponse));

		CompletableFuture<List<Stock>> result = stockService.updateStocksFromUpbitAsync();

		assertThrows(RuntimeException.class, result::join);
	}
}