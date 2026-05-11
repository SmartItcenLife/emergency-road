package com.itcen.emergencyroad.admin.controller;

import org.springframework.ui.Model;
import com.itcen.emergencyroad.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    //  커뮤니티 글 목록 조회
    @GetMapping("/posts")
    public String postList(Model model){
        model.addAttribute("postList", adminService.findAllPosts());
        return "admin/post-list";
    }

    // 채팅 들어가기?


}
