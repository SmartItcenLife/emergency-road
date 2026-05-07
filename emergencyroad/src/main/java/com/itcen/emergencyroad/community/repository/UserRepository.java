package com.itcen.emergencyroad.community.repository;

import com.itcen.emergencyroad.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUserName(String userName);

  boolean existsByNickname(String nickname);

  boolean existsByEmail(String email);
}
