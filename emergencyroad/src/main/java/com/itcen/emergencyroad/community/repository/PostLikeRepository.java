package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  boolean existsByPost(Post post);

  boolean existsByPost_IdAndUser_Id(Long postId, Long userId);

  void deleteByPost_IdAndUser_Id(Long postId, Long userId);

  long countByPost_Id(Long postId);
}
