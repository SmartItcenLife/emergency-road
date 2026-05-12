//package com.itcen.emergencyroad.findpath.controller;
//// 사용자의 요청을 받는 코드
//// 가장 먼저 실행됨
//
//import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
//import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
//import com.itcen.emergencyroad.findpath.service.PathService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController // 화면(View) 없이 데이터(JSON)만 반환하는 컨트롤러
//@RequestMapping("/api/path") // 기본 URL 주소 설정
//@RequiredArgsConstructor
//public class PathController {
//
//    // 비즈니스 로직 불러옴.
//    private final PathService pathService;
//
//    // POST 방식으로 사용자의 위치 데이터 받음.
//    @PostMapping("/hospitals")
//    public ResponseEntity<List<PathResponseDto>> findHospitals(@RequestBody LocationRequestDto requestDto) {
//
//        // 1. Service에게 사용자 위치(위도, 경도)를 전달함.
//        List<PathResponseDto> resultList = pathService.findHospitalsWithDistance(requestDto);
//
//        // 2. 계산된 결과(거리, 시간 등이 담긴 리스트)를 정상 상태(200 OK)로 반환합니다.
//        return ResponseEntity.ok(resultList);
//    }
//}
