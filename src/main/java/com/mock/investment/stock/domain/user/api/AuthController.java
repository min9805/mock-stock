package com.mock.investment.stock.domain.user.api;

import com.mock.investment.stock.domain.user.application.AuthService;
import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.domain.user.dto.*;
import com.mock.investment.stock.global.exception.ApiErrorResponse;
import com.mock.investment.stock.global.jwt.CurrentUser;
import com.mock.investment.stock.global.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = """
            # 회원가입
                        
            회원을 생성합니다.
            각 필드의 제약 조건은 다음과 같습니다.

            | 필드명 | 설명 | 제약조건 | 중복확인 | 예시 |
            |--------|------|----------|----------|------|
            | email | 사용자의 이메일 | 이메일 형식 | Y | email01@email.com |
            | name | 사용자의 이름 | 2~20자 | N | name01 |
            | password | 사용자의 비밀번호 | 영문(대소문자), 숫자, 특수문자를 포함한 8~32자 | N | password01! |
            | passwordCheck | 사용자의 비밀번호 확인 | password와 동일한 입력 | N | password01! |
             
            ## 응답
                        
            - 회원 가입 성공 시 `200` 코드와 함께 회원 기본 정보를 반환합니다.
            - 입력 양식에 오류가 있을 경우 `400` 에러를 반환합니다.
            - 중복된 값이 있을 경우 `409` 에러를 반환합니다.
             
            """)
    @ApiResponse(
            responseCode = "200",
            description = "생성한 계정 고유 번호를 반환합니다.",
            useReturnTypeSchema = true
    )
    @ApiResponse(
            responseCode = "409",
            description = "입력 값 중 중복된 값이 있습니다.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = "{\n  \"status\": \"CONFLICT\",\n  \"message\": \"데이터 중복\"\n}")
            )
    )
    public UserInfoDto createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return authService.createUser(createUserRequest);
    }


    @PostMapping("/login")
    @Operation(summary = "이메일 로그인", description = """
            # 로그인
                        
            사용자의 이메일과 비밀번호를 입력하여 로그인합니다.
                        
            ## 응답
                        
            - 로그인 성공 시 `200` 코드와 함께 유저 정보 및 토큰을 반환합니다.
              - 유저 정보엔는 email, name, role 이 포함되어 있습니다.
              - 토큰은 `accessToken`과 `refreshToken`으로 구성되어 있습니다.
            - 로그인 실패 시 `400` 에러를 반환합니다.
              - 계정이 존재하지 않거나 비밀번호가 일치하지 않을 경우 발생합니다.     
            """)
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공 시 유저 정보와 함께 토큰을 반환합니다.",
            useReturnTypeSchema = true
    )
    @ApiResponse(
            responseCode = "400",
            description = "로그인 실패",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "status": "BAD_REQUEST",
                                "message": "로그인에 실패했습니다."
                            }
                            """)
            )
    )
    public LoginResponseDto login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("reissue")
    @Operation(summary = "토큰 재발급", description = """
            # 토큰 재발급
                        
            리프레시 토큰을 이용하여 액세스 토큰을 재발급합니다.
                        
            ## 응답
                        
            - 토큰 재발급 성공 시 `200` 코드와 함께 새로운 액세스 토큰을 반환합니다.
            - 리프레시 토큰이 만료되었을 경우 `400` 에러를 반환합니다.
            """)
    @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공",
            useReturnTypeSchema = true
    )
    @ApiResponse(
            responseCode = "400",
            description = "토큰 재발급 실패",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "status": "BAD_REQUEST",
                                "message": "토큰 재발급에 실패했습니다."
                            }
                            """)
            )
    )
    public TokenDto reissue(@Valid @RequestBody ReissueRequest reissueRequest) {
        return authService.reissue(reissueRequest);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = """
            # 로그아웃
                        
            사용자를 로그아웃합니다.
                        
            ## 응답
                        
            - 로그아웃 성공 시 `200` 코드를 반환합니다.
            """)
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공",
            useReturnTypeSchema = true
    )
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "access-token")
    public void logout(@CurrentUser User user) {
        authService.logout(user);
    }
}