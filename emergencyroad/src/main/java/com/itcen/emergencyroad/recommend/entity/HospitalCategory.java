package com.itcen.emergencyroad.recommend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HospitalCategory {
    PEDIATRIC("소아 응급"),
    PREGNANT("임산부 응급"),
    GENERAL("일반 응급");

    private final String description; // 타임리프의 ${category.description}과 연결됨
}