package com.itcen.emergencyroad.recommend.controller;

import com.itcen.emergencyroad.recommend.dto.PediatricHospitalResponseDto;
import com.itcen.emergencyroad.recommend.dto.PregnantHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final HospitalRecommendationService recommendationService;

    @GetMapping("/rank")
    public String getRankings(@RequestParam HospitalCategory category,
                              @RequestParam Double lat,
                              @RequestParam Double lon,
                                  Model model) {

//        List<HospitalResponseDto> rankings =
//                recommendationService.getTop3Recommendations(category, lat, lon);
        model.addAttribute("category", category);
        model.addAttribute("locationProvided", true);
        model.addAttribute("displayLocation", "서울특별시 성동구");
        model.addAttribute("userLat", lat);
        model.addAttribute("userLon", lon);
        if (category == HospitalCategory.PEDIATRIC) {

            List<PediatricHospitalResponseDto> pediatricRankings =
                    recommendationService.getTop3Pediatric(lat, lon);

            model.addAttribute("rankings", pediatricRankings);
            model.addAttribute("userLat", lat);           // 카카오 길찾기 시작점 좌표
            model.addAttribute("userLon", lon);
            return "recommend/result-pediatric";
        }else if(category == HospitalCategory.GENERAL){
            //TODO
        }else if(category == HospitalCategory.PREGNANT) {
            List<PregnantHospitalResponseDto> pregnantRankings =
                    recommendationService.getTop3Pregnant(lat, lon);

            model.addAttribute("rankings", pregnantRankings);
            model.addAttribute("userLat", lat);           // 카카오 길찾기 시작점 좌표
            model.addAttribute("userLon", lon);
            return "recommend/result-pregnant";
        }
        return "result-pediatric";
    }
}



// 더미 데이터 리스트 생성
// 빌더 패턴을 사용하여 더미 데이터 생성
//        List<PediatricHospitalResponseDto> dummyList = new ArrayList<>();
//
//        // 1번 더미: 여유
//        dummyList.add(PediatricHospitalResponseDto.builder()
//                .hospitalName("한양대학교병원") // 부모 필드
//                .hpid("A1100008")           // 부모 필드
//                .distance(1.2)              // 부모 필드
//                .tags("24시간|소아응급전문의|야간진료") // 부모 필드
//                .congestionLabel("여유")     // 자식 필드
//                .availableBedPercentage(85.0)
//                .availablePediatricBedCount(12)
//                .hospitalLatitude(37.5594)
//                .hospitalLongitude(127.0453)
//                .build());
//
//        // 2번 더미: 보통
//        dummyList.add(PediatricHospitalResponseDto.builder()
//                .hospitalName("건국대학교병원")
//                .hpid("A1100003")
//                .distance(2.8)
//                .tags("소아전용|주차가능")
//                .congestionLabel("보통")
//                .availableBedPercentage(45.0)
//                .availablePediatricBedCount(5)
//                .hospitalLatitude(37.5408)
//                .hospitalLongitude(127.0718)
//                .build());
//
//        // 3번 더미: 혼잡
//        dummyList.add(PediatricHospitalResponseDto.builder()
//                .hospitalName("서울아산병원")
//                .hpid("A1100010")
//                .distance(4.5)
//                .tags("국내최대|대기김")
//                .congestionLabel("혼잡")
//                .availableBedPercentage(10.0)
//                .availablePediatricBedCount(1)
//                .hospitalLatitude(37.5266)
//                .hospitalLongitude(127.1086)
//                .build());

// 모델에 데이터 담기
//        model.addAttribute("rankings", rankings);
//        model.addAttribute("category", category);
//        model.addAttribute("locationProvided", true);
//        model.addAttribute("displayLocation", "서울특별시 성동구");
//        model.addAttribute("userLat", lat);
//        model.addAttribute("userLon", lon);

//        model.addAttribute("rankings", rankings);
//        model.addAttribute("category", category);
//
//        // HTML에서 사용하는 위치 관련 변수들 추가
//        model.addAttribute("locationProvided", true); // 현재 좌표를 받았으므로 true
//        model.addAttribute("userLat", lat);           // 카카오 길찾기 시작점 좌표
//        model.addAttribute("userLon", lon);           // 카카오 길찾기 시작점 좌표
//        model.addAttribute("displayLocation", "내 주변"); // 혹은 역지오코딩 서비스로 주소 변환 가능
