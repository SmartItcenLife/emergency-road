package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.PostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

  void deleteByPost_Id(Long postId);

  List<PostImage> findByPost_IdOrderByCreatedAtAsc(Long postId);

}
