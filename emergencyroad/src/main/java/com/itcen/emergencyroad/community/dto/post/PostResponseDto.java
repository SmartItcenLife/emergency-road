package com.itcen.emergencyroad.community.dto.post;

import com.itcen.emergencyroad.community.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

  private Long id;
  private String title;
  private String content;
  private String hpid;
  private String hospitalName;
  private String nickname;
  private boolean isDeleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostResponseDto from(Post post){
    PostResponseDto dto = new PostResponseDto();
    dto.id = post.getId();
    dto.title = post.getTitle();
    dto.content = post.getContent();
    dto.hpid = post.getHospital().getHpid();
    dto.hospitalName = post.getHospital().getHospitalName();
    dto.nickname = post.getUser().getNickname();
    dto.isDeleted = post.isDeleted();
    dto.createdAt = post.getCreatedAt();
    dto.updatedAt = post.getUpdatedAt();

    return dto;
  }
}
