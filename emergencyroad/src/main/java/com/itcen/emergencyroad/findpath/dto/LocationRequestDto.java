package com.itcen.emergencyroad.findpath.dto;
// 사용자가 서버에게 보내는 위도 경도 데이터

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDto {
    private Double originLat; // 사용자 위도
    private Double originLng; // 사용자 경도

    //private String destinationHpid; // 도착지를 지정해서 검색한다면 필요함. 이게 왜 필요하지?
}
