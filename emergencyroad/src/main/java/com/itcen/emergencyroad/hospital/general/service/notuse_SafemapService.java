package com.itcen.emergencyroad.hospital.general.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class notuse_SafemapService {

    public void getSafemapData() {
        try {
            // 1. 인증키 설정
            String serviceKey = "2W0DIKOO-2W0D-2W0D-2W0D-2W0DIKOOM4";

            // 2. URL 빌드 (데이터 조회용 data 주소)
            StringBuilder strBuilder = new StringBuilder("https://www.safemap.go.kr/openapi2/data");
            strBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey);
            strBuilder.append("&" + URLEncoder.encode("intId","UTF-8") + "=" + URLEncoder.encode("IF_0047", "UTF-8"));

            // 3. URL 객체 생성 및 연결 (이 부분이 누락되어 있었어요!)
            URL url = new URL(strBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            // 4. 응답 코드 확인 (200이면 성공)
            int responseCode = conn.getResponseCode();
            System.out.println("Safemap Response code: " + responseCode);

            BufferedReader rd;
            if(responseCode >= 200 && responseCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

            // 5. 데이터 읽기
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            // 6. 결과 출력 (콘솔에서 확인용)
            System.out.println("전송받은 데이터: " + sb.toString());

        } catch (Exception e) {
            System.out.println("생활안전지도 데이터 호출 중 에러 발생!");
            e.printStackTrace();
        }
    }
}