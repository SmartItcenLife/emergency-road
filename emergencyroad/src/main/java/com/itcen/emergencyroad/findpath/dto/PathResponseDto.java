package com.itcen.emergencyroad.findpath.dto;
// 서버가 사용자에게 보여줄 정보들

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // 빌더가 있어야 하는 이유는? -> PathService에 빌더 있음
public class PathResponseDto {

    private String hospitalName; // 병원 이름
    private String hpid; // 병원 아이디
    private Double distance; // 거리
    private Integer duration; // 소요 시간(분 단위)

}
