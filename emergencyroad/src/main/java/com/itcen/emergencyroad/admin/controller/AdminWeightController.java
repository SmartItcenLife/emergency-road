package com.itcen.emergencyroad.admin.controller;

import com.itcen.emergencyroad.recommend.dto.weight.GeneralWeightRequestDto;
import com.itcen.emergencyroad.recommend.dto.weight.GeneralWeightResponseDto;
import com.itcen.emergencyroad.recommend.dto.weight.PregnantWeightResponseDto;
import org.springframework.ui.Model;
import com.itcen.emergencyroad.admin.service.AdminWeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/weights")
@RequiredArgsConstructor
public class AdminWeightController {

    private final AdminWeightService adminWeightService;

    // 가중치 설정 페이지 조회
    @GetMapping
    public String getWeightConfigPage(Model model){
        GeneralWeightResponseDto generalWeight = adminWeightService.getGeneralWeight();
        PregnantWeightResponseDto pregnantWeight = adminWeightService.getPregnantWeight();

        model.addAttribute("generalWeight", generalWeight);
        model.addAttribute("pregnantWeight", pregnantWeight);

        return "admin/weight-config";
    }

    // 일반 응급 가중치 수정 요청 처리
    @PostMapping("/general")
    public String updateGeneralWeight(@ModelAttribute GeneralWeightRequestDto dto, RedirectAttributes redirectAttributes){
        try {
            adminWeightService.updateGeneralWeight(dto);
            // 수정한 뒤에 "저장되었습니다"라는 메시지를 화면에 띄우기 위해 사용합니다.
            redirectAttributes.addFlashAttribute("message", "일반 응급 가중치가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/admin/weights";
    }

    // 임산부 응급 가중치 수정 요청 처리
    @PostMapping("/pregnant")
    public String updatePregnantWeight(@ModelAttribute GeneralWeightRequestDto dto, RedirectAttributes redirectAttributes){
        try {
            adminWeightService.updateGeneralWeight(dto);
            // 수정한 뒤에 "저장되었습니다"라는 메시지를 화면에 띄우기 위해 사용합니다.
            redirectAttributes.addFlashAttribute("message", "일반 응급 가중치가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/admin/weights";
    }

}
