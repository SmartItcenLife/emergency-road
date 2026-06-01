package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.PostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

  void deleteByPost_Id(Long postId);

  List<PostImage> findByPost_IdOrderByCreatedAtAsc(Long postId);

  @Query("SELECT pi FROM PostImage pi WHERE pi.post.id IN :postIds ORDER BY pi.createdAt ASC")
  List<PostImage> findByPostIdIn(@Param("postIds") List<Long> postIds);

  List<Long> post(Post post);
}
