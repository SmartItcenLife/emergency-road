package com.itcen.emergencyroad.admin.dto;

import com.itcen.emergencyroad.community.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminPostListDTO {
    private Long id;
    private String hospitalName;
    private String title;
    private String userName;
    private LocalDateTime createdAt;
    private boolean isDeleted;

    public AdminPostListDTO(Post post){
        this.id = post.getId();
        this.hospitalName = post.getHospital().getHospitalName();
        this.title = post.getTitle();
        this.userName = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.isDeleted = post.isDeleted();
    }
}
