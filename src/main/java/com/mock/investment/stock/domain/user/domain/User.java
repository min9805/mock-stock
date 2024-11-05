package com.mock.investment.stock.domain.user.domain;

import com.mock.investment.stock.domain.user.dto.CreateUserRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
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
