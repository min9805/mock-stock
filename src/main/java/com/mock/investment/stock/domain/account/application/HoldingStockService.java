package com.mock.investment.stock.domain.account.application;

import com.mock.investment.stock.domain.account.dao.HoldingStockRepository;
import com.mock.investment.stock.domain.account.domain.HoldingStock;
import com.mock.investment.stock.domain.account.dto.HoldingStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldingStockService {
    private final HoldingStockRepository holdingStockRepository;

}
