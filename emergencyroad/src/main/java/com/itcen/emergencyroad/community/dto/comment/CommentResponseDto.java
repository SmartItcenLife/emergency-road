package com.itcen.emergencyroad.community.dto.comment;

import com.itcen.emergencyroad.community.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private Long id;
  private Long userId;
  private String nickname;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CommentResponseDto from(Comment comment){
    CommentResponseDto dto = new CommentResponseDto();
    dto.id = comment.getId();
    dto.userId = comment.getUser().getId();
    dto.nickname = comment.getUser().getNickname();
    dto.content = comment.getContent();
    dto.createdAt = comment.getCreatedAt();
    dto.updatedAt = comment.getCreatedAt();

    return dto;
  }
}
