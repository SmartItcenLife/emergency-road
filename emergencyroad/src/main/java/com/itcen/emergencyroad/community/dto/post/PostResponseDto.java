package com.itcen.emergencyroad.community.dto.post;

import com.itcen.emergencyroad.community.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class PostResponseDto {

  private Long id;
  private Long userId;
  private String title;
  private String content;
  private String hpid;
  private String hospitalName;
  private String nickname;
  private boolean isDeleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<String> imageUrls;

  public static PostResponseDto from(Post post, List<String> imageUrls){
    PostResponseDto dto = new PostResponseDto();
    dto.id = post.getId();
    dto.userId = post.getUser().getId();
    dto.title = post.getTitle();
    dto.content = post.getContent();
    dto.hpid = post.getHospital().getHpid();
    dto.hospitalName = post.getHospital().getHospitalName();
    dto.nickname = post.getUser().getNickname();
    dto.isDeleted = post.isDeleted();
    dto.createdAt = post.getCreatedAt();
    dto.updatedAt = post.getUpdatedAt();
    dto.imageUrls = imageUrls;
    return dto;
  }
}
