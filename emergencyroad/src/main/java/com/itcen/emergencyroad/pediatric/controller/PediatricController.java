package com.itcen.emergencyroad.pediatric.controller;

import com.itcen.emergencyroad.findpath.service.KakaoLocalApiClient;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.service.PediatricViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pediatric")
public class PediatricController {

    private final PediatricViewService pediatricViewService;
    private final KakaoLocalApiClient kakaoLocalApiClient;

    @GetMapping("/hospitals")
    public String hospitalList(@RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lon,
                               Model model){
        List<PediatricHospitalListDto> hospitals =
                pediatricViewService.getPediatricHospitalList(lat,lon);
        String displayLocation = kakaoLocalApiClient.getDisplayLocation(lat, lon);
        // 서울특별시 중구, 기본값
        double baseLat = lat != null ? lat : 37.5665;
        double baseLon = lon != null ? lon : 126.9780;

        model.addAttribute("hospitals",hospitals);
        model.addAttribute("locationProvided", lat != null && lon != null);
        model.addAttribute("displayLocation", displayLocation);
        model.addAttribute("userLat",baseLat);
        model.addAttribute("userLon",baseLon);

        return "pediatric/hospitals";
    }
}
