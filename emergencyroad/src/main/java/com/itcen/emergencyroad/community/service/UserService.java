package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.SignupRequestDto;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
}
