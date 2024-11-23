package com.mock.investment.stock.domain.order.api;

import com.mock.investment.stock.domain.order.dto.BuyOrderRequest;
import com.mock.investment.stock.domain.order.service.BuyOrderServiceImpl;
import com.mock.investment.stock.domain.user.application.UserDetailsImpl;
import com.mock.investment.stock.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BuyOrderMessageController {
	private final BuyOrderServiceImpl buyOrderServiceImpl;

	@MessageMapping("/order/buy")
	public void getStockPriceByWebsocket(@Payload BuyOrderRequest buyOrderRequest, Authentication authentication) {
		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

		buyOrderServiceImpl.createMarketOrder(user, buyOrderRequest);
	}
}