package com.itcen.emergencyroad.admin.controller;

import org.springframework.ui.Model;
import com.itcen.emergencyroad.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 메인 페이지
    @GetMapping({"", "/"})
    public String adminMain(){
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

    // 신고글/댓글 목록 보기
    @GetMapping("/reports")
    public String reportedList(Model model){
        model.addAttribute("reportList", adminService.findAllReports());
        return "admin/report-list";
    }
    // 신고버튼을 눌렀을 때의 상호작용은 post 컨트롤러에서 함


}
