package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(Long postId);

  @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.isDeleted = false")
  long countByPostIdAndIsDeletedFalse(@Param("postId") Long postId);
}
