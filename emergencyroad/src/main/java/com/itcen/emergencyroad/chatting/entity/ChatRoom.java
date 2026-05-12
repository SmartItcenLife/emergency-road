package com.itcen.emergencyroad.chatting.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatroom")
@Getter
@NoArgsConstructor

// 채팅방 엔티티
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 방 번호

    @Column(nullable = false)
    private String roomName; // 사용자에게 보여줄 방 이름

    private LocalDateTime createdAt = LocalDateTime.now();

}
