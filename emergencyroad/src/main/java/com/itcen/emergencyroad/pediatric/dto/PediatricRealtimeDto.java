package com.itcen.emergencyroad.pediatric.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PediatricRealtimeDto {

    private String hpid;          // 기관코드
    private String dutyName;      // 기관명

    private String hv10;          // 소아 인공호흡기
    private String hvventisoayn;  // 조산아용 인공호흡기 가용 여부
    private String hv11;          // 인큐베이터
    private String hvincuayn;     // 인큐베이터 가용 여부
    private String hv12;          // 소아당직의 직통연락처
    private String hv15;          // 소아 음압격리
    private String hv16;          // 소아 일반격리
    private String hv28;          // 소아 병상 수
    private String hv32;          // 소아 중환자실 수
    private String hv33;          // 응급전용 소아중환자실 수
    private String hv37;          // 응급전용 소아입원실 수
    private String hvidate;       // 입력일시
}
