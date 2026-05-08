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

@Service // 비즈니스 로직을 수행한다는 어노테이션
@RequiredArgsConstructor // 의존성 주입. 생성자를 만들어준다.
public class PathService {
    // RequiredArgsConstructor 어노테이션 하고 내가 필요한 객체 선언하면 자동으로 생성자 만들어짐.
    // 미리 만들어둔 객체를 가져다 꽂는 것.
    private final HospitalRepository hospitalRepository;
    private final KakaoMobilityApiClient kakaoApiClient;

    // 사용자 위치(매개변수) 받기 → DB에서 병원 찾기 → 카카오에게 보낼 주문서(JSON) 조립하기 → 카카오에 통신 지시하기 → 받아온 결과를 parseResponse에 넘기기
    public List<PathResponseDto> findHospitalsWithDistance(LocationRequestDto requestDto) {

        // 1. DB에서 가져오되, 데이터가 없으면 가짜 데이터를 만들기(지금 없으니까 만들어야댐
        List<Hospital> targetHospitals = hospitalRepository.findAll(); // 목적 병원 리스트
        if (targetHospitals.isEmpty()) {
            System.out.println("⚠️ DB가 비어있어 테스트용 가짜 데이터 만듦");
            // @Builder가 설정되어 있다고 가정. 만약 에러나면 new Hospital() 후 setter를 사용.
            targetHospitals.add(Hospital.builder()
                    .hpid("TEST01").hospitalName("가짜강남성심병원")
                    .longitude(126.9095).latitude(37.4912).build());
            targetHospitals.add(Hospital.builder()
                    .hpid("TEST02").hospitalName("가짜서울대학교병원")
                    .longitude(127.0000).latitude(37.5796).build());
        }

        // 2. JSON 바디에 사용자의 위도, 경도 값 넣기
        JSONObject requestBody = new JSONObject();
        JSONObject origin = new JSONObject();
        origin.put("x", requestDto.getOriginLng());
        origin.put("y", requestDto.getOriginLat());
        requestBody.put("origin", origin); // 바디에 위도, 경도 값 넣기

        // 3. 목적지 리스트
        JSONArray destinations = new JSONArray(); // 배열
        // 여기를 for 문 말고 parallelStream()으로 구현하면 병렬로 요청하기 때문에 빠르게 취합 가능
        for (Hospital hospital : targetHospitals) {
            // 좌표가 있는 것만 담기
            if (hospital.getLongitude() == null || hospital.getLatitude() == null) continue;
            JSONObject dest = new JSONObject();
            dest.put("x", hospital.getLongitude());
            dest.put("y", hospital.getLatitude());
            dest.put("key", hospital.getHpid());
            // ❌ 여기에 radius 안 들어감 !!!!
            destinations.put(dest); // 목적지 배열에 dest를 여러 개 넣음(다중 목적지니까)

            if (destinations.length() >= 30) break; // 최대 30개
        }

        // 최종적으로 바디에는 목적지 리스트, 반경 넣기
        requestBody.put("destinations", destinations);
        requestBody.put("radius", 9999);

        if (destinations.isEmpty()) {
            System.out.println("🚨 전송할 목적지가 하나도 없습니다.");
            return new ArrayList<>();
        }

        // 4. API 호출은 맨 마지막에
        String jsonResponse = kakaoApiClient.fetchDirections(requestBody);

        // 4. 응답 파싱 (기존 로직 유지)
        return parseResponse(jsonResponse, targetHospitals);
    }

    // 카카오가 던져준 긴 원본 텍스트(String)를 분석하기
    // 매개변수에는 카카오에서 응답 해 온 값, 목적지 병원 리스트 들어감
    private List<PathResponseDto> parseResponse(String jsonResponse, List<Hospital> targetHospitals) {
        List<PathResponseDto> resultList = new ArrayList<>();
        JSONObject responseObj = new JSONObject(jsonResponse);

        // 키 값이 routes이면 배열에서 꺼내라
        if (responseObj.has("routes")) {
            JSONArray routes = responseObj.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = routes.getJSONObject(i);
                if (route.getInt("result_code") != 0) continue;

                // hpid와 써머리에 키 값, 써머리 값 넣음
                String hpid = route.getString("key");
                JSONObject summary = route.getJSONObject("summary");

                // DB 병원 hpid와 카카오가 준 hpid가 일치하는 병원을 반환
                Hospital matched = targetHospitals.stream()
                        .filter(h -> h.getHpid().equals(hpid))
                        .findFirst().orElse(null);

                //병원 이름, hpid, 거리, 소요 시간을 직관적으로 넣을 수 있음.
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