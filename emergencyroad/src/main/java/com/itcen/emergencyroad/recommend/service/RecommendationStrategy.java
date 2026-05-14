package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.recommend.entity.HospitalCategory;


public interface RecommendationStrategy {

    HospitalCategory getCategory();

    void calculateScores();
}
