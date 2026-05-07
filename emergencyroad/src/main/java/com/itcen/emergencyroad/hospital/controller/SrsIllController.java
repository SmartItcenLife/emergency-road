package com.itcen.emergencyroad.hospital.controller;

import com.itcen.emergencyroad.hospital.entity.Srsill;
import com.itcen.emergencyroad.hospital.repository.SrsillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/srsill")
public class SrsIllController {

    // 중증 질환 정보를 저장할 SrsillRepository만 사용
    private final SrsillRepository srsillRepository;

    @ResponseBody
    @GetMapping("/fetch")
    public String fetchAndSaveSrsillData() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // 주의: 실제 운영 시 서비스 키는 application.properties 등에 숨기는 것이 좋다.
            String serviceKey = "752ac9989c794c8264aac315792e3b46e797f3f0e8512a3f6a7c72167c5f2561";
            String stage1 = URLEncoder.encode("서울특별시", "UTF-8");

            // 🌟 오직 중증 질환 수용 정보 API 주소만 사용
            String diseaseInfoUrl = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getSrsillDissAceptncPosblInfoInqire?serviceKey=" + serviceKey + "&STAGE1=" + stage1 + "&pageNo=1&numOfRows=300";

            Document diseaseDoc = dBuilder.parse(diseaseInfoUrl);
            diseaseDoc.getDocumentElement().normalize();
            NodeList diseaseList = diseaseDoc.getElementsByTagName("item");

            for (int temp = 0; temp < diseaseList.getLength(); temp++) {
                Node nNode = diseaseList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    // 병원 고유 코드(hpid) 추출
                    String hpid = getTagValue("hpid", eElement);
                    if(hpid == null) continue;

                    // Srsill 테이블에서 hpid로 검색 후 없으면 새로 생성
                    // (주의: Srsill 엔티티에 findByHpid를 쓰시려면 SrsillRepository에 Optional<Srsill> findByHpid(String hpid) 메서드가 있어야 합니다)
                    Srsill srsill = srsillRepository.findByHpid(hpid).orElse(new Srsill());

                    // (선택) Srsill 엔티티에 hpid 필드를 추가하셨다면 아래 주석을 해제하세요.
                    // srsill.setHpid(hpid);

                    // --- 일반 질환 ---
                    srsill.setMKioskTy1(getTagValue("MKioskTy1", eElement)); // 심근경색
                    srsill.setMKioskTy2(getTagValue("MKioskTy2", eElement)); // 뇌경색
                    srsill.setMKioskTy3(getTagValue("MKioskTy3", eElement)); // 거미막하 출혈
                    srsill.setMKioskTy4(getTagValue("MKioskTy4", eElement)); // 거미막하출혈 외
                    srsill.setMKioskTy5(getTagValue("MKioskTy5", eElement)); // 대동맥응급_흉부
                    srsill.setMKioskTy6(getTagValue("MKioskTy6", eElement)); // 대동맥응급_복부
                    srsill.setMKioskTy23(getTagValue("MKioskTy23", eElement)); // 응급투석
                    srsill.setMKioskTy24(getTagValue("MKioskTy24", eElement)); // 폐쇄병동입원
                    srsill.setMKioskTy11(getTagValue("MKioskTy11", eElement)); // 응급내시경-성인위장관
                    srsill.setMKioskTy13(getTagValue("MKioskTy13", eElement)); // 응급내시경-성인기관지
                    srsill.setMKioskTy19(getTagValue("MKioskTy19", eElement)); // 중증화상-전문치료
                    srsill.setMKioskTy26(getTagValue("MKioskTy26", eElement)); // 영상의학혈관중재-성인

                    // --- 임산부 ---
                    srsill.setMKioskTy22(getTagValue("MKioskTy22", eElement)); // 응급투석
                    srsill.setMKioskTy16(getTagValue("MKioskTy16", eElement)); // 산부인과응급 분만
                    srsill.setMKioskTy17(getTagValue("MKioskTy17", eElement)); // 산부인과응급 산과수술
                    srsill.setMKioskTy18(getTagValue("MKioskTy18", eElement)); // 산부인과응급 부인과수술

                    // --- 소아 ---
                    srsill.setMKioskTy10(getTagValue("MKioskTy10", eElement)); // 장충첩/폐색_영유아
                    srsill.setMKioskTy12(getTagValue("MKioskTy12", eElement)); // 응급내시경_영유아 위장관
                    srsill.setMKioskTy14(getTagValue("MKioskTy14", eElement)); // 응급내시경_영유아_기관지
                    srsill.setMKioskTy15(getTagValue("MKioskTy15", eElement)); // 저체중 출산아
                    srsill.setMKioskTy27(getTagValue("MKioskTy27", eElement)); // 영상의학혈관중재_영유아

                    // Srsill 테이블에 바로 저장
                    srsillRepository.save(srsill);
                }
            }

            System.out.println("중증 질환 수용 가능 정보(Srsill) 파싱 및 DB 저장 완료!");
            return "redirect:/home";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    // XML 태그에서 값을 안전하게 꺼내오는 내부 헬퍼 메서드
    private String getTagValue(String tag, Element eElement) {
        NodeList nList = eElement.getElementsByTagName(tag);
        if (nList != null && nList.getLength() > 0) {
            Node node = nList.item(0);
            if (node != null && node.getChildNodes().getLength() > 0) {
                return node.getChildNodes().item(0).getNodeValue();
            }
        }
        return null;
    }
}