package com.itcen.emergencyroad.general.service;

import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.general.dto.GeneralHospitalDetailDto;
import com.itcen.emergencyroad.general.dto.GeneralHospitalListDto;
import com.itcen.emergencyroad.general.repository.GeneralRepository;
import com.itcen.emergencyroad.recommend.dto.GeneralHospitalResponseDto;
import com.itcen.emergencyroad.recommend.dto.HospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalSortType;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralViewService {
    private final GeneralRepository generalRepository;
    private final HospitalRecommendationService hospitalRecommendationService;


    /*
        기존 코드 : 추천 결과 목록을 만들자마자 반환 ( sort 할 수 없음 )
        변경 코드 : 추천결과 조회 -> DTO 목록으로 변환 -> sortType 기준으로 정렬 -> 반환
     */
    public List<GeneralHospitalListDto> getGeneralHospitalList(
            Double lat,
            Double lon,
            HospitalSortType sortType
    ) {

        List<HospitalResponseDto> recommendations =
                hospitalRecommendationService.getRecommendations(
                        HospitalCategory.GENERAL,
                        lat,
                        lon,false
                );
        recommendations.forEach(dto ->
                System.out.println(
                        dto.getClass().getName()
                ));
        /*
            기존 코드 : DTO 변환이 끝나면 바로 반환하여 정렬을 적용할 수 없음
            이에 따라 리스트 변수에 담은 뒤 정렬 후처리를 진행 후 반환 하도록 로직 변경
         */
        List<GeneralHospitalListDto> hospitals = recommendations.stream()
                .filter(dto ->
                        dto instanceof GeneralHospitalResponseDto)
                .map(dto -> {

                    GeneralHospitalResponseDto gDto =
                            (GeneralHospitalResponseDto) dto;
                    System.out.println(
                            gDto.getHospitalName()
                                    + " | "
                                    + gDto.getAvailableEmergencyBedCount()
                                    + " | "
                                    + gDto.getTotalEmergencyBedCount()
                                    + " | "
                                    + gDto.getDistance()
                                    + " | "
                                    + gDto.getDuration()
                    );
                    return GeneralHospitalListDto.builder()
                            .hpid(gDto.getHpid())
                            .hospitalName(
                                    gDto.getHospitalName()
                            )
                            .availableEmergencyBedCount(
                                    gDto.getAvailableEmergencyBedCount()
                            )
                            .totalEmergencyBedCount(
                                    gDto.getTotalEmergencyBedCount()
                            )
                            .emergencyPhone(
                                    gDto.getEmergencyPhone()
                            )
                            .hospitalLatitude(
                                    gDto.getHospitalLatitude()
                            )
                            .hospitalLongitude(
                                    gDto.getHospitalLongitude()
                            )
                            .distanceKm(
                                    gDto.getDistance()
                            )
                            .duration(gDto.getDuration())
                            .recordedAt(
                                    gDto.getRecordedAt()
                            )
                            .build();
                })
                .collect(Collectors.toList());

        sortGeneralHospitals(hospitals, sortType,lat, lon);

        return hospitals;
    }
    // 정렬 기준에 따른 정렬 메서드
    private void sortGeneralHospitals(
            List<GeneralHospitalListDto> hospitals,
            HospitalSortType sortType,
            Double lat,
            Double lon
    ) {
        if (sortType == HospitalSortType.BED) {
            // 병상 여유순은 가용 병상 비율이 높은 병원부터 보여줌
            hospitals.sort(
                    Comparator.comparing(
                            (GeneralHospitalListDto hospital) -> hospital.getAvailableBedPercentage(),
                            Comparator.nullsLast(Comparator.reverseOrder())
                    ).thenComparing(
                            (GeneralHospitalListDto hospital) -> hospital.getAvailableEmergencyBedCount(),
                            Comparator.nullsLast(Comparator.reverseOrder())
                    )
            );
        }
        if (sortType == HospitalSortType.DISTANCE){
            // 거리순은 API 호출 비용을 제한하기 위해 가까운 일부 병원만 도로거리/시간을 조회
            // API 결과가 없는 병원은 HospitalRecommendationService의 resolveRouteInfo 로 계산진행
            Map<String, PathResponseDto> routeMap =
                    hospitalRecommendationService.getDistanceAndDurationMap(
                            HospitalCategory.GENERAL,
                            lat,
                            lon,
                            30
                    );
            hospitals.forEach(hospital -> {
                PathResponseDto path = routeMap.get(hospital.getHpid());

                if ( path != null ) {
                    // routeMap의 결과를 목록 DTO에 반영해야 화면의 거리/시간 표시와 정렬이 같은 값을 사용한다.
                    hospital.updateRouteInfo(
                            path.getDistance(),
                            path.getDuration()
                    );
                }
            });

            // 가까운 병원부터 보여주기 위해 거리 오름차순으로 정렬
            hospitals.sort(
                    Comparator.comparing(
                            hospital -> hospital.getDistanceKm(),
                            Comparator.nullsLast(Comparator.naturalOrder())
                    )
            );
        }
    }

    public GeneralHospitalDetailDto getGeneralHospitalDetail(String hpid) {
        return generalRepository.findGeneralHospitalDetail(hpid)
                .orElseThrow(() -> new IllegalArgumentException("일반 병원 상세 정보가 없습니다. hpid=" + hpid));
    }
}
