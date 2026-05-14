package com.itcen.emergencyroad.recommend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class GeneralHospitalResponseDto extends HospitalResponseDto {
    private String emergencyLevel;
    private Integer availableBedCount;
}