package com.mock.investment.stock.domain.account.api;

import com.mock.investment.stock.domain.account.application.AccountService;
import com.mock.investment.stock.domain.account.dto.AccountBalanceDto;
import com.mock.investment.stock.domain.account.dto.SimpleAccountDto;
import com.mock.investment.stock.global.jwt.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/")
    public SimpleAccountDto getAccountList(@CurrentUser Long userId) {
        return accountService.findByUserId(userId);
    }
}
