package com.mock.investment.stock.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email
    @Schema(description = "사용자 이메일", example = "user01@email.com")
    private final String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Schema(description = "사용자 비밀번호", example = "password123")
    private final String password;
}
