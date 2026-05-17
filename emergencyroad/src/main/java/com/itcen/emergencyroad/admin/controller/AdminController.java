package com.itcen.emergencyroad.admin.controller;

import com.itcen.emergencyroad.admin.dto.DashboardResponseDto;
import com.itcen.emergencyroad.community.dto.ReportResponseDTO;
import org.springframework.ui.Model;
import com.itcen.emergencyroad.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 메인 페이지 대시보드
    @GetMapping({"", "/"})
    public String adminMain(Model model) {
        // 서비스에서 통계 바구니를 가져옵니다.
        DashboardResponseDto stats = adminService.getDashboardStats();

        // "stats"라는 이름으로 화면에 던져줍니다.
        model.addAttribute("stats", stats);

        return "admin/admin-main";
    }

    // 회원 목록 보기
    @GetMapping("/users")
    public String userList(Model model){
        model.addAttribute("userList", adminService.findAllUsers());
        return "admin/user-list";
    }
    // 회원 삭제하기
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id){
        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    //  커뮤니티 글 목록 조회
    @GetMapping("/posts")
    public String postList(Model model){
        model.addAttribute("postList", adminService.findAllPosts());
        return "admin/post-list";
    }
    //커뮤니티 글 삭제하기
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id){
        adminService.deletePost(id);
        return "redirect:/admin/posts";
    }
    // 커뮤니티 제목 누르면 상세 페이지 들어가기
    // 이건 세빈님 코드에 구현되어 있음.

    // 신고글/댓글 목록 보기 (게시글/댓글 분리 버전!)
    @GetMapping("/reports")
    public String reportedList(Model model){
        List<ReportResponseDTO> allReports = adminService.findAllReports();

        // 1. 게시글 신고만 쏙 골라내기
        List<ReportResponseDTO> postReports = allReports.stream()
                .filter(r -> r.getTargetType() == com.itcen.emergencyroad.community.enums.ReportTargetType.POST)
                .collect(Collectors.toList());

        // 2. 댓글 신고만 쏙 골라내기
        List<ReportResponseDTO> commentReports = allReports.stream()
                .filter(r -> r.getTargetType() == com.itcen.emergencyroad.community.enums.ReportTargetType.COMMENT)
                .collect(Collectors.toList());

        // 화면에 각각 다른 이름표로 던져줍니다.
        model.addAttribute("postReportList", postReports);
        model.addAttribute("commentReportList", commentReports);

        return "admin/report-list";
    }

    // 신고된 게시글/댓글 강제 삭제 처리
    @PostMapping("/reports/{reportId}/delete")
    public String deleteReportedTarget(@PathVariable Long reportId) {
        adminService.deleteReportedTarget(reportId);
        return "redirect:/admin/reports"; // 삭제 후 다시 신고 목록으로 새로고침
    }


}
