package com.itcen.emergencyroad.community.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class PostRequestDto {

  @NotBlank(message = "제목을 입력해주세요")
  @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요")
  private String content;

  private String hospitalId;
}
