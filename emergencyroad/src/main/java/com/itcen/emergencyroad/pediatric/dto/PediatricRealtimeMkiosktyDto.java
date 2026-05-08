package com.itcen.emergencyroad.pediatric.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PediatricRealtimeMkiosktyDto {
    private String hpid;          // 기관코드
    private String dutyName;      // 기관명

    private String mkioskty10;          // 장중첩/폐색 영유아
    private String mkioskty12;  // 응급내시경 영유아 위장관
    private String mkioskty14;          // 응급내시경 영유아 기관지
    private String mkioskty15;     // 저체중출생아
    private String mkioskty27;          // 영상의학혈관중재 영유아
    private String mkioskty10Msg;         // 장중첩/폐색 영유아 메세지
    private String mkioskty12Msg; // 응급내시경 영유아 위장관 메세지
    private String mkioskty14Msg;         // 응급내시경 영유아 기관지 메세지
    private String mkioskty15Msg;    // 저체중출생아 메세지
    private String mkioskty27Msg;         // 영상의학혈관중재 영유아 메세지
}