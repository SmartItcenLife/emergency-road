package com.itcen.emergencyroad.admin.dto;
// 관리자 페이지 서비스와 컨트롤러부터 구현하자는 페이지에 들어갈 회원 정보
import com.itcen.emergencyroad.community.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUserResponseDTO {
    private Long id;
    private String userName;
    private String nickname;
    private String role;
    private LocalDateTime createdAt;

    public AdminUserResponseDTO(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }
}
