package com.itcen.emergencyroad.findpath.service;

import com.itcen.emergencyroad.findpath.api.KakaoMobilityApiClient;
import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {

    private final HospitalRepository hospitalRepository;
    private final KakaoMobilityApiClient kakaoApiClient;

    public List<PathResponseDto> findHospitalsWithDistance(LocationRequestDto requestDto) {

        // 1. DB에서 가져오되, 데이터가 없으면 가짜 데이터를 만듭니다.
        List<Hospital> targetHospitals = hospitalRepository.findAll();

        if (targetHospitals.isEmpty()) {
            System.out.println("⚠️ DB가 비어있어 테스트용 가짜 데이터를 생성합니다.");
            // @Builder가 설정되어 있다고 가정합니다. 만약 에러나면 new Hospital() 후 setter를 사용하세요.
            targetHospitals.add(Hospital.builder()
                    .hpid("TEST01").hospitalName("강남성심병원")
                    .longitude(126.9095).latitude(37.4912).build());
            targetHospitals.add(Hospital.builder()
                    .hpid("TEST02").hospitalName("서울대학교병원")
                    .longitude(127.0000).latitude(37.5796).build());
        }

        // 2. JSON 조립
        JSONObject requestBody = new JSONObject();

        // 2-1. 출발지
        JSONObject origin = new JSONObject();
        origin.put("x", requestDto.getOriginLng());
        origin.put("y", requestDto.getOriginLat());
        requestBody.put("origin", origin);

        // 2-2. 목적지 리스트
        JSONArray destinations = new JSONArray();
        for (Hospital hospital : targetHospitals) {
            // 좌표가 있는 것만 담기
            if (hospital.getLongitude() == null || hospital.getLatitude() == null) continue;

            JSONObject dest = new JSONObject();
            dest.put("x", hospital.getLongitude());
            dest.put("y", hospital.getLatitude());
            dest.put("key", hospital.getHpid());
            // ❌ 여기에 radius를 넣지 않습니다.
            destinations.put(dest);

            if (destinations.length() >= 30) break;
        }

        requestBody.put("destinations", destinations);

        // 💡 중요: radius는 여기서 "최상위"에 딱 한 번만 넣습니다!
        requestBody.put("radius", 9999);

        if (destinations.isEmpty()) {
            System.out.println("🚨 전송할 목적지가 하나도 없습니다.");
            return new ArrayList<>();
        }

        // 3. API 호출
        String jsonResponse = kakaoApiClient.fetchDirections(requestBody);

        // 4. 응답 파싱 (기존 로직 유지)
        return parseResponse(jsonResponse, targetHospitals);
    }

    private List<PathResponseDto> parseResponse(String jsonResponse, List<Hospital> targetHospitals) {
        List<PathResponseDto> resultList = new ArrayList<>();
        JSONObject responseObj = new JSONObject(jsonResponse);

        if (responseObj.has("routes")) {
            JSONArray routes = responseObj.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = routes.getJSONObject(i);
                if (route.getInt("result_code") != 0) continue;

                String hpid = route.getString("key");
                JSONObject summary = route.getJSONObject("summary");

                Hospital matched = targetHospitals.stream()
                        .filter(h -> h.getHpid().equals(hpid))
                        .findFirst().orElse(null);

                if (matched != null) {
                    resultList.add(PathResponseDto.builder()
                            .hospitalName(matched.getHospitalName())
                            .hpid(hpid)
                            .distanceKm(Math.round((summary.getInt("distance") / 1000.0) * 10) / 10.0)
                            .durationmin(summary.getInt("duration") / 60)
                            .build());
                }
            }
        }
        return resultList;
    }
}