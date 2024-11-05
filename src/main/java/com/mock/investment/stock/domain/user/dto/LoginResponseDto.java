package com.mock.investment.stock.domain.user.dto;

import com.mock.investment.stock.global.jwt.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    UserInfoDto user;
    TokenDto token;
}
