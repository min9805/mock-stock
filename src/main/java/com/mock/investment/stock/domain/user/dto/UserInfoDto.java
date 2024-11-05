package com.mock.investment.stock.domain.user.dto;

import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.domain.user.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    @Schema(description = "사용자 이메일", example = "user01@email.com")
    private String email;
    @Schema(description = "사용자 이름", example = "name01")
    private String name;
    @Schema(description = "권한", example = "ROLE_USER")
    private UserRole userRole;

    public static UserInfoDto fromEntity(User user) {
        return UserInfoDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}