package com.mock.investment.stock.domain.order.api;

import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.dto.OrderRequest;
import com.mock.investment.stock.domain.order.service.BuyOrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order/buy")
@RequiredArgsConstructor
public class BuyOrderController {
    private final BuyOrderServiceImpl buyOrderServiceImpl;

    @PostMapping("/market")
    public BuyOrderDto createBuyOrder(
            @RequestBody BuyOrderRequest buyOrderRequest
    ){
        return buyOrderServiceImpl.createMarketOrder(buyOrderRequest);
    }

    @GetMapping("/")
    public List<BuyOrderDto> getBuyOrders(
            @RequestBody OrderRequest orderRequest
    ){
        return buyOrderServiceImpl.getOrders(orderRequest);
    }
}
