package com.itcen.emergencyroad.chatting.entity;


import com.itcen.emergencyroad.community.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="chatmessage")
@NoArgsConstructor

// 대화 내용 엔티티
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom; // 어느 방에서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender; // 누가

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 뭐라고 보냈는지

    private LocalDateTime sendAt = LocalDateTime.now(); // 언제
}
