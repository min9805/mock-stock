package com.mock.investment.stock.domain.stock.api;

import com.mock.investment.stock.domain.stock.application.StockService;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@Tag(name = "Stock")
public class StockController {
	private final StockService stockService;

	@GetMapping("update")
	public List<StockDto> updateStocksFromUpbitAsync() throws ExecutionException, InterruptedException, TimeoutException {
		return stockService.updateStocksFromUpbitAsync().get(5, TimeUnit.SECONDS);
	}


}