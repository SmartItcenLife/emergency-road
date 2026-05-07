package com.itcen.emergencyroad.external.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageApiDto {
    private String hpid;          // 기관코드
    private String dutyName;      // 기관명
    private String dutyAddr;      // 주소

    private String symTypCod;     // 중증질환구분 코드
    private String symTypCodMag;  // 중증질환명
    private String symBlkMsgTyp;  // 메시지구분
    private String symBlkMsg;     // 전달메시지
    private String symBlkSttDtm;  // 차단시작시간
    private String symBlkEndDtm;  // 차단종료시간
    private String symOutDspYon;  // 표출 여부
    private String symOutDspMth;  // 표출 방식
}
