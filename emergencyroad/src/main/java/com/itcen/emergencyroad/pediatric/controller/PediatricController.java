package com.itcen.emergencyroad.pediatric.controller;

import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.service.PediatricViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pediatric")
public class PediatricController {

    private final PediatricViewService pediatricViewService;

    @GetMapping("/hospitals")
    public String hospitalList(Model model){
        List<PediatricHospitalListDto> hospitals =
                pediatricViewService.getPediatricHospitalList();

        model.addAttribute("hospitals",hospitals);

        return "pediatric/hospitals";
    }
}
