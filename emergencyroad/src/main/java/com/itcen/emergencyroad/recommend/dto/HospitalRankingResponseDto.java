package com.itcen.emergencyroad.recommend.dto;

import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import lombok.Builder;
import lombok.Getter;

//사용자에게 보여줄 최종 결과물(순위, 거리, 합산 점수 등)만 담기 위해 만든 전용 객체
@Getter
@Builder
public class HospitalRankingResponseDto {
    private String hospitalName;    // 병원 이름
    private String hpid;            // 병원 코드
    double distance;              //사용자 → 병원까지 실제 거리 (km)
    double duration;              //사용자가 병원까지 이동하는 예상 시간 (분)
    private Double finalScore;      // 로직 점수 + 거리 점수
    private String tags;            // "분만가능 | NICU보유" 등
    private String address;         // 주소 (화면 출력용)


    public static HospitalRankingResponseDto of(HospitalScore score, double distance, double duration, double finalScore) {
        return HospitalRankingResponseDto.builder()
                .hospitalName(score.getHospital().getHospitalName())
                .hpid(score.getHospital().getHpid())
                .distance(distance)
                .duration(duration)
                .finalScore(finalScore)
                .tags(score.getPregnantTags())
                .address(score.getHospital().getAddress())
                .build();
    }
}
