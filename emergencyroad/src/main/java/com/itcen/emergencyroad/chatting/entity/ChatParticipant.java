package com.itcen.emergencyroad.chatting.entity;

import com.itcen.emergencyroad.community.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chatparticipant")
@Getter
@NoArgsConstructor

// 채팅 참여자 엔티티
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 여러 참여자가 하나의 채팅방에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom;

    // community의 유저 테이블 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

}
