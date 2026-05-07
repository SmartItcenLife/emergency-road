package com.itcen.emergencyroad.findpath.dto;
// 서버가 사용자에게 보여줄 정보들

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PathResponseDto {

    private String hospitalName; // 병원 이름
    private String hpid; // 병원 아이디
    private Double distanceKm; // 거리
    private Integer durationmin; // 소요 시간(분 단위)

}
