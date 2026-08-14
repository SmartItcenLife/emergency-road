package com.itcen.emergencyroad.recommend.service;

import org.springframework.stereotype.Component;

@Component
public class PediatricCongestionCalculator {

//    public String getLabel(Integer bed, Integer total) {
//
//        if (bed == null || total == null || total == 0) {
//            return "정보없음";
//        }
//
//        double ratio = (double) bed / total * 100;
//
//        if (ratio >= 70) return "여유";
//        if (ratio >= 30) return "보통";
//        return "혼잡";
//    }

    public double getPercentage(Integer bed, Integer total) {
        if (bed == null || total == 0) return 0.0;
        return (double) bed / total * 100;
    }

    public String getLabel(Integer bed, Integer total) {
        if (bed == null || total == null) return "";

        // 1. 병상이 절대적으로 부족하면 무조건 '주의' 또는 '혼잡'
        if (bed <= 2) {
            return "주의";
        }

        // 2. 병상이 넉넉할 때만 퍼센트로 판단
        double percent = (bed * 100.0) / total;
        if (percent >= 50) return "여유";
        if (percent >= 20) return "보통";

        return "혼잡";
    }
}
