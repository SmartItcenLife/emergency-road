package com.itcen.emergencyroad.recommend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    // 추천 시작 화면 (버튼 있는 페이지)
    @GetMapping("/home")
    public String recommendHome() {
        return "home";
    }
}
