package com.itcen.emergencyroad.recommend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public abstract class HospitalResponseDto {
    private String hospitalName;
    private String hpid;
    private Double distance;
    private Double duration;
    private Double finalScore;
    private String address;
    private String tags;

    // 필요하다면 공통 메서드를 여기에 작성할 수 있습니다.
    public String getFormattedDistance() {
        return String.format("%.2fkm", this.distance);
    }
}