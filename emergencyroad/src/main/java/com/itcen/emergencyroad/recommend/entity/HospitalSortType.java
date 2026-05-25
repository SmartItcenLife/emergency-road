package com.itcen.emergencyroad.recommend.entity;

// 여러 화면에서 공유되는 정렬 기준을 문자열로 반복 처리하지 않고,
// 하나의 enum으로 관리하기 위해 만든 클래스
public enum HospitalSortType {
    SCORE,
    DISTANCE,
    BED;

    public static HospitalSortType from(String value) {
        if (value == null || value.isBlank()) return SCORE;

        try {
            return HospitalSortType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return SCORE;
        }
    }
}
