package com.itcen.emergencyroad.general.controller;

import com.itcen.emergencyroad.findpath.service.KakaoLocalApiClient;
import com.itcen.emergencyroad.general.dto.GeneralHospitalListDto;
import com.itcen.emergencyroad.general.service.GeneralViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/general")
public class GeneralController {
    private final GeneralViewService generalViewService;
    private final KakaoLocalApiClient kakaoLocalApiClient;

    @GetMapping("/hospitals")
    public String hospitalList(@RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lon,
                               Model model) {

        double baseLat = (lat != null && lat != 0.0) ? lat : 37.5665;
        double baseLon = (lon != null && lon != 0.0) ? lon : 126.9780;

        List<GeneralHospitalListDto> hospitals = generalViewService.getGeneralHospitalList(baseLat, baseLon);

        String displayLocation =
                kakaoLocalApiClient.getDisplayLocation(baseLat, baseLon);

        model.addAttribute("hospitals", hospitals);
        model.addAttribute("locationProvided", lat != null && lon != null);
        model.addAttribute("displayLocation", displayLocation);
        model.addAttribute("userLat", baseLat);
        model.addAttribute("userLon", baseLon);

        return "general/hospitals";
    }
}
