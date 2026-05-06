package com.itcen.emergencyroad.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Column(name = "content", nullable = false, length = 500)
  private String content;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = true)
  private LocalDateTime updatedAt;

  @PrePersist
  public void onCreate(){
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  public void onUpdate(){
    this.updatedAt = LocalDateTime.now();
  }

  public static Comment create(Post post, User user, String content){
    Comment comment = new Comment();
    comment.post = post;
    comment.user = user;
    comment.content = content;

    return comment;
  }

  public void update(String content){
    this.content = content;
  }

  public void delete(){
    this.isDeleted = true;
  }
}
