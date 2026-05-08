package com.itcen.emergencyroad.community.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccountDto kakaoAccount;

  public String getNickname(){
    return kakaoAccount != null && kakaoAccount.getProfile() != null
        ? kakaoAccount.getProfile().getNickname() : null;
  }

  public String getProfileImageUrl(){
    return kakaoAccount != null && kakaoAccount.getProfile() != null
        ? kakaoAccount.getProfile().getProfileImageUrl() : null;
  }

}
