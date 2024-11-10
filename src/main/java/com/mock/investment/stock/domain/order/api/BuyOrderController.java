package com.mock.investment.stock.domain.order.api;

import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.dto.OrderRequest;
import com.mock.investment.stock.domain.order.service.BuyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order/buy")
@RequiredArgsConstructor
public class BuyOrderController {
    private final BuyOrderService buyOrderService;

    @PostMapping("/market")
    public BuyOrderDto createBuyOrder(
            @RequestBody BuyOrderRequest buyOrderRequest
    ){
        return buyOrderService.createMarketBuyOrder(buyOrderRequest);
    }

    @GetMapping("/")
    public List<BuyOrderDto> getBuyOrders(
            @RequestBody OrderRequest orderRequest
    ){
        return buyOrderService.getBuyOrders(orderRequest);
    }
}
