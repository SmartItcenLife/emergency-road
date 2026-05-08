package com.itcen.emergencyroad.community.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class SignupRequestDto {

  @NotBlank(message = "아이디는 입력해야 합니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$",
      message = "아이디는 영문과 숫자를 둘다 포함한 조합으로 6~20자로 입력해주세요"
  )
  private String userName;

  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
      message = "비밀번호는 영문+숫자+특수문자 8자 이상으로 입력해주세요"
  )
  private String password;

  @NotBlank(message = "닉네임을 입력해야합니다.")
  @Size(max = 30, message = "닉네임은 30글자 이내로 입력해야합니다.")
  private String nickname;

  @NotBlank(message = "이메일을 입력해야합니다.")
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  private String email;

  private String profileImageUrl;

}
