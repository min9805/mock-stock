package com.mock.investment.stock.domain.order.api;

import com.mock.investment.stock.domain.order.dto.BuyOrderDto;
import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.dto.OrderRequest;
import com.mock.investment.stock.domain.order.service.BuyOrderServiceImpl;
import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.global.jwt.CurrentUser;
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
            @CurrentUser User user,
            @RequestBody BuyOrderRequest buyOrderRequest
    ){
        return buyOrderServiceImpl.createMarketOrder(user, buyOrderRequest);
    }

    @GetMapping("/")
    public List<BuyOrderDto> getBuyOrders(
            @RequestBody OrderRequest orderRequest
    ){
        return buyOrderServiceImpl.getOrders(orderRequest);
    }
}