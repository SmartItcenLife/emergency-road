package com.itcen.emergencyroad.chatting.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String content;
    private LocalDateTime sendAt;
}
