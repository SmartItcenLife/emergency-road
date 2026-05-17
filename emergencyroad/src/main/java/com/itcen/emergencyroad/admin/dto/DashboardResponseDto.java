package com.itcen.emergencyroad.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardResponseDto {
    private long todayUsers;   // 오늘 가입 회원
    private long todayPosts;   // 오늘 새 게시글
    private long totalReports; // 현재 신고 접수 건
}