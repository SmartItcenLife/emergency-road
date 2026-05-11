package com.itcen.emergencyroad.chatting.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class ChatRoomDTO {
    private Long id;
    private String roomName;
    private LocalDateTime createdAt;
}
