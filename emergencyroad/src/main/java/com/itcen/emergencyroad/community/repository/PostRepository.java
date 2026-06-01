package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.PostImage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(value = "SELECT p FROM Post p JOIN FETCH p.user JOIN FETCH p.hospital " +
      "WHERE p.hospital.hpid = :hpid AND p.isDeleted = false",
      countQuery = "SELECT COUNT(p) FROM Post p WHERE p.hospital.hpid = :hpid AND p.isDeleted = false")
  Page<Post> findByHospitalHpidAndIsDeletedFalse(String hpid, Pageable pageable);

  @Query("select p from Post p where p.hospital.hpid = :hpid and p.isDeleted = false " +
      "and (p.title like %:keyword% or p.content like %:keyword%)")
  Page<Post> searchByHospitalId(@Param("hpid") String hpid, @Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT pi FROM PostImage pi WHERE pi.post.id IN :postIds ORDER BY pi.createdAt ASC")
  List<PostImage> findByPostIdIn(@Param("postIds") List<Long> postIds);

  long countByCreatedAtAfter(java.time.LocalDateTime startOfDay);
}
