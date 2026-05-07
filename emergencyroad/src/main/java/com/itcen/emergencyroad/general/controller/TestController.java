//package com.itcen.emergencyroad.general.controller;
//
//import com.itcen.emergencyroad.general.service.GeneralService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class TestController {
//
//    private final GeneralService generalService;
//
//    // 브라우저에서 이 주소로 접속하면 파싱 로직이 실행됩니다.
//    @GetMapping("/test/fetch-hospital")
//    public String testFetch() {
//        generalService.fetchAndSaveHospitalData();
//        return "데이터 파싱 및 DB 저장 명령을 실행했습니다! 인텔리제이 콘솔창과 DB를 확인해 보세요.";
//    }
//
//}