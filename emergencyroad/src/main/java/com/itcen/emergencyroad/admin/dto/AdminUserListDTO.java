//package com.itcen.emergencyroad.admin.dto;
//// 관리자용 회원 목록 DTO
//// 화면에 회원 비밀 번호같은 민감한 정보가 넘어가지 않도록, 필요한 정보만 담는 바구니
//
//import com.itcen.emergencyroad.community.entity.User;
//import com.itcen.emergencyroad.community.enums.Role;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//
//@Getter
//public class AdminUserListDTO {
//    private Long id;
//    private String userName;
//    private String nickname;
//    private Role role;
//    private LocalDateTime createdAt;
//
//    // 엔티티를 DTO로 변환해주는 생성자
//    public AdminUserListDTO(User user){
//        this.id = user.getId();
//        this.userName = user.getUserName();
//        this.nickname = user.getNickname();
//        this.role = Role.USER;
//        this.createdAt = user.getCreatedAt();
//    }
//}
