package com.itcen.emergencyroad.community.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequestDto {

  @Size(max = 30, message = "닉네임은 30자 이하로 입력해주세요.")
  private String nickname;

  private String profileImageUrl;
}
