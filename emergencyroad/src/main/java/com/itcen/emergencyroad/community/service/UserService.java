package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.LoginRequestDto;
import com.itcen.emergencyroad.community.dto.SignupRequestDto;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /***
   *
   * @param dto
   */
  @Transactional
  public void signUp(SignupRequestDto dto){
      if(userRepository.existsByUserName(dto.getUserName())){
        throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
      }

      if(userRepository.existsByNickname(dto.getNickname())){
        throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
      }

      if(userRepository.existsByEmail(dto.getEmail())){
        throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
      }

      log.info(dto.getPassword());
      log.info(passwordEncoder.encode(dto.getPassword()));

      String encodedPw = passwordEncoder.encode(dto.getPassword());

    User user = User.createLocalUser(
      dto.getUserName(),
      encodedPw,
      dto.getNickname(),
      dto.getEmail(),
      dto.getProfileImageUrl()
    );

    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public void login(LoginRequestDto dto, HttpSession session){

    User user = userRepository.findByUserName(dto.getUserName());

    if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    session.setAttribute("loginUser", user.getId());
    session.setAttribute("loginNickname", user.getNickname());
    session.setAttribute("loginRole", user.getRole().name());
  }
}
