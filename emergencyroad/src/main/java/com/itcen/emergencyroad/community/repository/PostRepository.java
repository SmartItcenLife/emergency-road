package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByHospitalHpidAndIsDeletedFalse(String hpid, Pageable pageable);

  @Query("select p from Post p where p.hospital.hpid = :hpid and p.isDeleted = false " +
  "and (p.title like %:keyword% or p.content like %:keyword%)")
  Page<Post> searchByHospitalId(@Param("hpid") String hpid, @Param("keyword") String keyword, Pageable pageable);
}
