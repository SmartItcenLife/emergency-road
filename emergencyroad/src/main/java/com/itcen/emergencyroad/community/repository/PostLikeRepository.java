package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.PostLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  boolean existsByPost(Post post);

  boolean existsByPost_IdAndUser_Id(Long postId, Long userId);

  void deleteByPost_IdAndUser_Id(Long postId, Long userId);

  @Query("SELECT pl.post.id, COUNT(pl) FROM PostLike pl WHERE pl.post.id IN :postIds GROUP BY pl.post.id")
  List<Object[]> countByPost_Id(@Param("postIds") List<Long> postIds);

  long countByPost_Id(Long postId);
}
