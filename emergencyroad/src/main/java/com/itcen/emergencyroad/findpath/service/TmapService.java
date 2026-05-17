package com.itcen.emergencyroad.findpath.service;

import com.itcen.emergencyroad.findpath.api.TmapApiClient;
import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TmapService {

    private final TmapApiClient tmapApiClient;

    public List<PathResponseDto> findHospitalsWithDistanceTmap(LocationRequestDto requestDto, List<Hospital> hospitals) {

        // 1. 위도/경도가 모두 있는 유효한 병원만 추려내기 (응답 결과와 매칭하기 위함)
        List<Hospital> validHospitals = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            if (hospital.getLongitude() != null && hospital.getLatitude() != null) {
                validHospitals.add(hospital);
                // 티맵 매트릭스는 출발지 1개일 때 목적지 최대 50개까지 권장하므로 제한을 50개로 둡니다.
                if (validHospitals.size() >= 50) break;
            }
        }

        if (validHospitals.isEmpty()) {
            log.warn("🚨 전송할 목적지가 하나도 없습니다.");
            return new ArrayList<>();
        }

        // 2. Tmap 경로 매트릭스 규격에 맞춰 JSON 바디(주문서) 조립
        JSONObject requestBody = new JSONObject();

        // [출발지 배열 조립]
        JSONArray origins = new JSONArray();
        JSONObject origin = new JSONObject();
        origin.put("lon", String.valueOf(requestDto.getOriginLng())); // 사용자 경도
        origin.put("lat", String.valueOf(requestDto.getOriginLat())); // 사용자 위도
        origins.put(origin);
        requestBody.put("origins", origins);

        // [목적지 배열 조립]
        JSONArray destinations = new JSONArray();
        for (Hospital hospital : validHospitals) {
            JSONObject dest = new JSONObject();
            dest.put("lon", String.valueOf(hospital.getLongitude())); // 병원 경도
            dest.put("lat", String.valueOf(hospital.getLatitude()));  // 병원 위도
            destinations.put(dest);
        }
        requestBody.put("destinations", destinations);

        // 3. API 통신 호출
        log.info("티맵 다중 목적지 매트릭스 API 호출 시작 (목적지 개수: {})", validHospitals.size());
        String jsonResponse = tmapApiClient.fetchMatrixDirections(requestBody);

        // 4. 받아온 결과 파싱하기
        return parseTmapResponse(jsonResponse, validHospitals);
    }

    // 티맵이 던져준 결과를 파싱하는 로직
    private List<PathResponseDto> parseTmapResponse(String jsonResponse, List<Hospital> validHospitals) {
        List<PathResponseDto> resultList = new ArrayList<>();
        JSONObject responseObj = new JSONObject(jsonResponse);

        // 응답 객체에 matrixRoutes가 있는지 확인
        if (responseObj.has("matrixRoutes")) {
            JSONArray matrixRoutes = responseObj.getJSONArray("matrixRoutes");

            for (int i = 0; i < matrixRoutes.length(); i++) {
                JSONObject route = matrixRoutes.getJSONObject(i);

                // 경로를 찾지 못하는 등 에러가 발생한 목적지는 스킵 (성공 시 "Ok" 반환)
                if (route.has("status") && !route.getString("status").equals("Ok")) continue;

                // 💡 핵심 로직: 응답의 destinationIndex를 사용해 어떤 병원인지 매칭!
                // validHospitals 배열에 넣은 순서 그대로 Index 번호가 반환됩니다.
                int destIndex = route.getInt("destinationIndex");
                Hospital matchedHospital = validHospitals.get(destIndex);

                // 티맵 응답 데이터 (거리는 m, 소요시간은 sec 단위)
                double distanceKm = route.getInt("distance") / 1000.0;
                int durationMin = route.getInt("duration") / 60;

                // 소수점 첫째 자리까지만 표시되도록 반올림 (예: 1.5km)
                distanceKm = Math.round(distanceKm * 10) / 10.0;

                // DTO 조립 후 리스트에 담기
                resultList.add(PathResponseDto.builder()
                        .hospitalName(matchedHospital.getHospitalName())
                        .hpid(matchedHospital.getHpid())
                        .distance(distanceKm)
                        .duration(durationMin)
                        .build());
            }
        } else {
            log.error("Tmap API 응답 형식이 예상과 다릅니다. 원본: {}", jsonResponse);
        }

        return resultList;
    }
}