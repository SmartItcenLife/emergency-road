package com.itcen.emergencyroad.pregnant.controller;

import com.itcen.emergencyroad.findpath.service.KakaoLocalApiClient;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalListDto;
import com.itcen.emergencyroad.pregnant.service.PregnantViewService;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pregnant")
public class PregnantController {

    private final PregnantViewService pregnantViewService;

    private final HospitalRecommendationService hospitalRecommendationService;
    private final KakaoLocalApiClient kakaoLocalApiClient;

    // 전체 병원 목록 출력
    @GetMapping("/hospitals")
    public String hospitalList(@RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lon,
                               Model model) {

        double baseLat = (lat != null && lat != 0.0) ? lat : 37.5665;
        double baseLon = (lon != null && lon != 0.0) ? lon : 126.9780;

        List<PregnantHospitalListDto> list =
                pregnantViewService.getPregnantHospitalList(baseLat, baseLon);

        String displayLocation = kakaoLocalApiClient.getDisplayLocation(baseLat, baseLon);

        model.addAttribute("hospitals", list);
        model.addAttribute("locationProvided", lat != null && lon != null);
        model.addAttribute("displayLocation", displayLocation);
        model.addAttribute("userLat", baseLat);
        model.addAttribute("userLon", baseLon);

        return "pregnant/hospitals";
    }

    @GetMapping("/hospitals/{hpid}/detail")
    @ResponseBody
    public ResponseEntity<PregnantHospitalDetailDto> getHospitalDetail(@PathVariable String hpid){
        PregnantHospitalDetailDto detail =
                pregnantViewService.findPregnantRealtimeByHospital(hpid);
        return ResponseEntity.ok(detail);
    }
}
