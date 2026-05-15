package com.itcen.emergencyroad.pediatric.controller;

import com.itcen.emergencyroad.findpath.service.KakaoLocalApiClient;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalDetailDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.service.PediatricViewService;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pediatric")
public class PediatricController {

    private final PediatricViewService pediatricViewService;
    private final KakaoLocalApiClient kakaoLocalApiClient;
    private final HospitalRecommendationService hospitalRecommendationService;

    @GetMapping("/hospitals")
    public String hospitalList(@RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lon,
                               Model model){
        // 1. 먼저 null 체크를 해서 기본값을 확정짓습니다.
        double baseLat = (lat != null && lat != 0.0) ? lat : 37.5665;
        double baseLon = (lon != null && lon != 0.0) ? lon : 126.9780;
//
//        List<PediatricHospitalListDto> hospitals =
//                pediatricViewService.getPediatricHospitalList(lat,lon);
          // 서울특별시 중구, 기본값
        List<PediatricHospitalListDto> list = hospitalRecommendationService.getPediatricHospitalList(baseLat, baseLon);

        System.out.println("전달된 lat: " + lat + ", 결정된 baseLat: " + baseLat);

        String displayLocation = kakaoLocalApiClient.getDisplayLocation(baseLat, baseLon);

        model.addAttribute("hospitals", list);
        model.addAttribute("locationProvided", lat != null && lon != null);
        model.addAttribute("displayLocation", displayLocation); //뺄 예정
        model.addAttribute("userLat", baseLat);
        model.addAttribute("userLon", baseLon);

        return "pediatric/hospitals";
    }

    @GetMapping("/hospitals/{hpid}/detail")
    @ResponseBody
    public ResponseEntity<PediatricHospitalDetailDto> getHospitalDetail(@PathVariable String hpid){
        PediatricHospitalDetailDto detail =
                pediatricViewService.getPediatricHospitalDetail(hpid);
        return ResponseEntity.ok(detail);
    }

}
