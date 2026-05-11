package com.itcen.emergencyroad.pediatric.controller;

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

    @GetMapping("/hospitals")
    public String hospitalList(@RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lon,
                               Model model){
        List<PediatricHospitalListDto> hospitals =
                pediatricViewService.getPediatricHospitalList(lat,lon);

        model.addAttribute("hospitals",hospitals);
        model.addAttribute("locationProvided", lat != null && lon != null);

        return "pediatric/hospitals";
    }
}
