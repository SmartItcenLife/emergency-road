package com.itcen.emergencyroad.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    private String password;

    private String nickname;

    private String email;

    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}