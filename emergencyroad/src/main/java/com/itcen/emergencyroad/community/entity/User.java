package com.itcen.emergencyroad.community.entity;

import com.itcen.emergencyroad.community.enums.LoginType;
import com.itcen.emergencyroad.community.enums.Role;
import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", unique = true, nullable = true, length = 20)
    private String userName;

    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "nickname", nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(name = "email", nullable = true, length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LoginType loginType;

    @Column(nullable = true, length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = true, length = 50)
    private String kakaoId;

    public void updateKakaoProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
