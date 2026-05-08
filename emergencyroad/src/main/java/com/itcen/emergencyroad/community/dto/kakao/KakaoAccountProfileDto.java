package com.itcen.emergencyroad.community.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAccountProfileDto {

  /***
   * {
   * 카카오 디벨로퍼스 응답 예시
   *    "id": 123456789,
   *    "kakao_account": {
   *       "profile": {
   *        "nickname": "세빈",
   *        "profile_image_url": "https://..."
   *      }
   *     }
   *
   */
  private String nickname;

  @JsonProperty("profile_image_url")
  private String profileImageUrl;

}
