package com.mock.investment.stock.domain.user.domain;

import com.mock.investment.stock.domain.user.dto.CreateUserRequest;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRole userRole;

    @Column(nullable = false)
    @Convert(converter = OAuthProviderConverter.class)
    private OAuthProvider oAuthProvider;

    // 이메일로 회원가입
    public User(CreateUserRequest dto, String encodedPassword) {
        this.email = dto.getEmail();
        this.name = dto.getName();
        this.password = encodedPassword;
        this.userRole = UserRole.USER;
        this.oAuthProvider = OAuthProvider.LOCAL;
    }
}