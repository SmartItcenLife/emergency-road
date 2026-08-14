package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.CommentLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  boolean existsByComment_IdAndUser_Id(Long commentId, Long userId);

  void deleteByComment_IdAndUser_Id(Long commentId, Long userId);

  long countByComment_Id(Long commentId);

  @Query("SELECT cl.comment.id, COUNT(cl) FROM CommentLike cl WHERE cl.comment.id IN :commentIds GROUP BY cl.comment.id")
  List<Object[]> countByCommentIdIn(@Param("commentIds") List<Long> commentIds);

  @Query("SELECT cl.comment.id FROM CommentLike cl WHERE cl.comment.id IN :commentIds AND cl.user.id = :userId")
  List<Long> findLikedCommentIdsByUserIdIn(@Param("commentIds") List<Long> commentIds, @Param("userId") Long userId);
}
