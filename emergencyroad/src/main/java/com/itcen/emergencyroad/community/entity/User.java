package com.itcen.emergencyroad.community.entity;

import com.itcen.emergencyroad.community.enums.LoginType;
import com.itcen.emergencyroad.community.enums.Role;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = true, length = 50)
    private String kakaoId;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    public static User createLocalUser(String userName, String encodedPw, String nickname, String email, String profileImageUrl){
        User user = new User();
        user.userName = userName;
        user.password = encodedPw;
        user.nickname = nickname;
        user.email = email;
        user.profileImageUrl = profileImageUrl;
        user.loginType = LoginType.LOCAL;
        user.role = Role.USER;

        return user;
    }

    public static User createKakaoUser(String kakaoId, String nickname, String profileImageUrl){
      User user = new User();
      user.kakaoId = kakaoId;
      user.nickname = nickname;
      user.profileImageUrl = profileImageUrl;
      user.loginType = LoginType.KAKAO;
      user.role = Role.USER;

      return user;
    }

    public void updateKakaoProfile(String nickname, String profileImageUrl) {

        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void updateNickname(@Size(max = 30, message = "닉네임은 30자 이하로 입력해주세요.") String nickname) {
        this.nickname = nickname;
    }
}
