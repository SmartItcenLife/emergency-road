package com.itcen.emergencyroad.recommend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HospitalCategory {
    GENERAL {
        @Override
        public double getScore(HospitalScore score) {
            return score.getGeneralScore();
        }
    },

    PEDIATRIC {
        @Override
        public double getScore(HospitalScore score) {
            return score.getPediatricScore();
        }
    },

    PREGNANT {
        @Override
        public double getScore(HospitalScore score) {
            return score.getPregnantScore();
        }
    };

    public abstract double getScore(HospitalScore score);
}