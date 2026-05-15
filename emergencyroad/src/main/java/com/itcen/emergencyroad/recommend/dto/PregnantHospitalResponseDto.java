package com.itcen.emergencyroad.recommend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class PregnantHospitalResponseDto extends HospitalResponseDto {
    private String deliveryAvailable;
    private Integer nicuAvailable;
}