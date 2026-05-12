package com.itcen.emergencyroad.recommend.controller;

import com.itcen.emergencyroad.recommend.dto.HospitalRankingResponseDto;
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

        List<HospitalRankingResponseDto> rankings =
                recommendationService.getTop3Recommendations(category, lat, lon);


        model.addAttribute("rankings", rankings);
        model.addAttribute("category", category);

        return "recommend/result";
    }
}