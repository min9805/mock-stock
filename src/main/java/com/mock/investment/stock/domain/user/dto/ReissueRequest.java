package com.mock.investment.stock.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReissueRequest {
    @NotBlank
    @Schema(description = "리프레시 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDFAZW1haWwuY29tIiwiZXhwIjoxNzE1MDg5Nzc4LCJpYXQiOjE3MTUwMDMzNzh9.GK01DHHZYxYc5FXl_c5Aq0qJg9YbUoSl-U6YCj8L7ik")
    private String refreshToken;
}
