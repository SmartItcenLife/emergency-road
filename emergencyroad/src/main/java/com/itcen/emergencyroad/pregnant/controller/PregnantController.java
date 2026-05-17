package com.itcen.emergencyroad.pregnant.controller;

import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.service.PregnantViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequiredArgsConstructor
@RequestMapping("/pregnant")
public class PregnantController {

    private final PregnantViewService pregnantViewService;

    @GetMapping("/hospitals/{hpid}/detail")
    @ResponseBody
    public ResponseEntity<PregnantHospitalDetailDto> getHospitalDetail(@PathVariable String hpid){
        PregnantHospitalDetailDto detail =
                pregnantViewService.findPregnantRealtimeByHospital(hpid);
        return ResponseEntity.ok(detail);
    }
}
