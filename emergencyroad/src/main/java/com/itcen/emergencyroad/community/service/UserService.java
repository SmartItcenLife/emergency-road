package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.LoginRequestDto;
import com.itcen.emergencyroad.community.dto.SignupRequestDto;
import com.itcen.emergencyroad.community.dto.UpdateUserRequestDto;
import com.itcen.emergencyroad.community.dto.kakao.KakaoUserInfoDto;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.enums.LoginType;
import com.itcen.emergencyroad.community.enums.Role;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final KakaoService kakaoService;
  private final ProfileImageService profileImageService;

  @Transactional
  public void signUp(SignupRequestDto dto){
      if(userRepository.existsByUserName(dto.getUserName())){
        throw new CustomException(ExceptionStatus.DUPLICATED_USERNAME);
      }

      if(userRepository.existsByNickname(dto.getNickname())){
        throw new CustomException(ExceptionStatus.DUPLICATED_NICKNAME);
      }

      if(userRepository.existsByEmail(dto.getEmail())){
        throw new CustomException(ExceptionStatus.DUPLICATED_EMAIL);
      }

      String encodedPw = passwordEncoder.encode(dto.getPassword());

      User user = User.builder()
          .userName(dto.getUserName())
          .role(Role.USER)
          .password(encodedPw)
          .nickname(dto.getNickname())
          .email(dto.getEmail())
          .profileImageUrl(dto.getProfileImageUrl())
          .loginType(LoginType.LOCAL)
          .build();

    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public void login(LoginRequestDto dto, HttpSession session){

    User user = userRepository.findByUserName(dto.getUserName())
        .orElseThrow(() -> new CustomException(ExceptionStatus.AUTHENTICATION_FAIL));

    if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
      throw new CustomException(ExceptionStatus.AUTHENTICATION_FAIL);
    }

    setSession(session, user, null);
  }

  @Transactional
  public void kakaoLogin(String code, HttpSession session){

    String accessToken = kakaoService.getAccessToken(code);
    KakaoUserInfoDto kakaoUserInfo = kakaoService.getUserInfo(accessToken);

    String kakaoId = String.valueOf(kakaoUserInfo.getId());

    User user = userRepository.findByKakaoId(kakaoId)
        .orElseGet(() -> {
          String nickname = kakaoUserInfo.getNickname();

          if (nickname == null || nickname.isBlank()) {
            nickname = "kakao_" + kakaoId.substring(0, Math.min(kakaoId.length(), 8));
          }

          if(userRepository.existsByNickname(nickname)){
            // 카카오 닉네임은 최대 20자여서 _(1자)와 UUID(9자)를 합치면 딱 30자가 맞춰집니다.
            nickname = nickname + "_" + UUID.randomUUID().toString().substring(0,9);
          }

          User newUser = User.builder()
              .userName(nickname)
              .nickname(nickname)
              .kakaoId(kakaoId)
              .role(Role.USER)
              .loginType(LoginType.KAKAO)
              .profileImageUrl(kakaoUserInfo.getProfileImageUrl())
              .build();

          return userRepository.save(newUser);

        });

    String kakaoNickname = kakaoUserInfo.getNickname();
    if (kakaoNickname != null && !kakaoNickname.isBlank()) {
      user.updateKakaoProfile(kakaoNickname, kakaoUserInfo.getProfileImageUrl());
    }

    setSession(session, user,accessToken);
  }

  public void logout(HttpSession session){
    String accessToken = (String) session.getAttribute("kakaoAccessToken");

    if(accessToken != null) kakaoService.logout(accessToken);
    session.invalidate();
  }

  @Transactional
  public void updateUser(Long userId, UpdateUserRequestDto dto, MultipartFile profileImage,
      HttpSession session){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

    if(dto.getNickname() != null && !dto.getNickname().isBlank()){
      if(!dto.getNickname().equals(user.getNickname()) &&
      userRepository.existsByNickname(dto.getNickname())){
        throw new CustomException(ExceptionStatus.DUPLICATED_NICKNAME);
      }
      user.updateNickname(dto.getNickname());
    }

    if(profileImage != null && !profileImage.isEmpty()){
      String imageUrl = profileImageService.uploadProfileImage(profileImage);
      user.updateProfileImage(imageUrl);
    }

    setSession(session, user, (String) session.getAttribute("kakaoAccessToken"));
  }

  @Transactional(readOnly = true)
  public User getUser(Long userId){
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.NOT_FOUND));
  }

  private void setSession(HttpSession session, User user, String kakaoAccessToken){
    session.setAttribute("loginUser", user.getId());
    session.setAttribute("loginNickname", user.getNickname());
    session.setAttribute("loginRole", user.getRole().name());
    session.setAttribute("loginProfileImage", user.getProfileImageUrl());
    session.setAttribute("kakaoAccessToken", kakaoAccessToken);
  }
}
