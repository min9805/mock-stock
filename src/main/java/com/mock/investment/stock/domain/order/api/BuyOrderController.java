package com.mock.investment.stock.domain.order.api;

import com.mock.investment.stock.domain.order.service.BuyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order/buy")
@RequiredArgsConstructor
public class BuyOrderController {
    private final BuyOrderService buyOrderService;

}
