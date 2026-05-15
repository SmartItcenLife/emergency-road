package com.itcen.emergencyroad.recommend.service;

import org.springframework.stereotype.Component;

@Component
public class PediatricCongestionCalculator {

    public String getLabel(Integer bed, Integer total) {

        if (bed == null || total == null || total == 0) {
            return "정보없음";
        }

        double ratio = (double) bed / total * 100;

        if (ratio >= 70) return "여유";
        if (ratio >= 30) return "보통";
        return "혼잡";
    }

    public double getPercentage(Integer bed, Integer total) {
        if (bed == null || total == 0) return 0.0;
        return (double) bed / total * 100;
    }
}
