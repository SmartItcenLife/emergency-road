package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  boolean existsByComment_IdAndUser_Id(Long commentId, Long userId);

  void deleteByComment_IdAndUser_Id(Long commentId, Long userId);

  long countByComment_Id(Long commentId);
}
