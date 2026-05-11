package com.itcen.emergencyroad.community.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentRequestDto {

  @NotBlank(message = "댓글 내용을 입력해주세요")
  @Size(max = 500, message = "댓글은 500자 이내로 입력해주세요.")
  private String content;
}
