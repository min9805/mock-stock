package com.mock.investment.stock.domain.account.api;

import com.mock.investment.stock.domain.account.application.HoldingStockService;
import com.mock.investment.stock.domain.account.dto.HoldingStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/holding")
@RequiredArgsConstructor
public class HoldingStockController {
    private final HoldingStockService holdingStockService;

}
