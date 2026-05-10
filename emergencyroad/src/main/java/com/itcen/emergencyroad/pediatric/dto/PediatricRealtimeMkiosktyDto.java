package com.itcen.emergencyroad.pediatric.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PediatricRealtimeMkiosktyDto {
    private String hpid;                    // 기관코드
    private String dutyName;                // 기관명

    @JsonProperty("MKioskTy10")
    private String mkioskty10; // 장중첩/폐색 영유아

    @JsonProperty("MKioskTy12")
    private String mkioskty12; // 응급내시경 영유아 위장관

    @JsonProperty("MKioskTy14")
    private String mkioskty14; // 응급내시경 영유아 기관지

    @JsonProperty("MKioskTy15")
    private String mkioskty15; // 저체중출생아

    @JsonProperty("MKioskTy27")
    private String mkioskty27; // 영상의학혈관중재 영유아

    @JsonProperty("MKioskTy10Msg")
    private String mkioskty10Msg;

    @JsonProperty("MKioskTy12Msg")
    private String mkioskty12Msg;

    @JsonProperty("MKioskTy14Msg")
    private String mkioskty14Msg;

    @JsonProperty("MKioskTy15Msg")
    private String mkioskty15Msg;

    @JsonProperty("MKioskTy27Msg")
    private String mkioskty27Msg;
}