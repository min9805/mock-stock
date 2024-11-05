package com.mock.investment.stock.global.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "토큰 정보")
@Getter
public class TokenDto {
    @Schema(description = "액세스 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDFAZW1haWwuY29tIiwiZXhwIjoxNzE1MDA2OTc4LCJpYXQiOjE3MTUwMDMzNzh9.4mVSMEiBNmbD0VXo0w5ZtOl45Qu5V1a4dZEArkibbIQ")
    String accessToken;
    @Schema(description = "리프레시 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDFAZW1haWwuY29tIiwiZXhwIjoxNzE1MDg5Nzc4LCJpYXQiOjE3MTUwMDMzNzh9.GK01DHHZYxYc5FXl_c5Aq0qJg9YbUoSl-U6YCj8L7ik")
    String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
