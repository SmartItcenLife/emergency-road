package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.repository.PediatricRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PediatricViewService {
    private final PediatricRealtimeRepository pediatricRealtimeRepository;

    // 기본 위치 지정
    private static final double DEFAULT_LAT = 37.5665;
    private static final double DEFAULT_LON = 126.9780;

    // Query 결과 조회
    public List<PediatricHospitalListDto> getPediatricHospitalList(Double lat, Double lon){
        List<PediatricHospitalListDto> hospitals =
            pediatricRealtimeRepository.findPediatricHospitalList();

        double baseLat = lat != null ? lat : DEFAULT_LAT;
        double baseLon = lon != null ? lon : DEFAULT_LON;

        for (PediatricHospitalListDto hospital : hospitals) {
            if(hospital.getHospitalLatitude() == null || hospital.getHospitalLongitude() == null){
                continue;
            }
            double distanceKm = calculateDistanceKm(baseLat, baseLon, hospital.getHospitalLatitude(),hospital.getHospitalLongitude());
            hospital.updateDistanceKm(Math.round(distanceKm * 10) / 10.0);
        }
        hospitals.sort(Comparator.comparing(
                hospital -> hospital.getDistanceKm(),
                Comparator.nullsLast((d1,d2) -> d1.compareTo(d2))
            )
        );
        return hospitals;
    }
    // 거리 계산 메서드
    private double calculateDistanceKm(
            double userLat,
            double userLon,
            double hospitalLat,
            double hospitalLon
    ){
        final double earthRadiusKm = 6371.0;

        double latDistance = Math.toRadians(hospitalLat - userLat);
        double lonDistance = Math.toRadians(hospitalLon - userLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat))
                * Math.cos(Math.toRadians(hospitalLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }
}
