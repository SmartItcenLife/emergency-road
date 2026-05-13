package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import java.util.List;

public interface RecommendationStrategy {

    HospitalCategory getCategory();

    void calculateScores();
}
