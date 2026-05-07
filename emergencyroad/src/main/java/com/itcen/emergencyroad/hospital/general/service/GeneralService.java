package com.itcen.emergencyroad.hospital.general.service;

import com.itcen.emergencyroad.hospital.general.entity.General;
import com.itcen.emergencyroad.hospital.general.repository.GeneralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class GeneralService {

    private final GeneralRepository generalRepository;

    @Transactional
    public void fetchAndSaveHospitalData() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            String serviceKey = "752ac9989c794c8264aac315792e3b46e797f3f0e8512a3f6a7c72167c5f2561";

            // 한글 주소를 URL에 맞게 인코딩
            String stage1 = URLEncoder.encode("서울특별시", "UTF-8");
            //String stage2 = URLEncoder.encode("송파구", "UTF-8");

            // 1. 병상 정보 API URL
            String bedInfoUrl = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire?serviceKey=" + serviceKey + "&STAGE1=" + stage1  + "&pageNo=1&numOfRows=300";

            // 2. 중증 질환 수용 정보 API URL
            String diseaseInfoUrl = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getSrsillDissAceptncPosblInfoInqire?serviceKey=" + serviceKey + "&STAGE1=" + stage1 +  "&pageNo=1&numOfRows=300";

            // =================================================================
            // 첫 번째 API: 병상 정보 파싱 및 저장[cite: 1]
            // =================================================================
            Document bedDoc = dBuilder.parse(bedInfoUrl);
            bedDoc.getDocumentElement().normalize();
            NodeList bedList = bedDoc.getElementsByTagName("item");

            for (int temp = 0; temp < bedList.getLength(); temp++) {
                Node nNode = bedList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String hpid = getTagValue("hpid", eElement); // 병원 코드 추출[cite: 1]
                    if(hpid == null) continue;

                    // DB에 이미 해당 병원이 있으면 불러오고, 없으면 새로 생성
                    General general = generalRepository.findByHpid(hpid).orElse(new General());
                    general.setHpid(hpid); // 필수 기준값 셋팅

                    // 병상 정보 셋팅
                    general.setErGeneralRealTimeBeds(getTagValue("hvec", eElement));
                    general.setErGeneralTotalBeds(getTagValue("hvs01", eElement));
                    general.setIcuGeneralRealTimeBeds(getTagValue("hvicc", eElement));
                    general.setIcuGeneralTotalBeds(getTagValue("hvs17", eElement));
                    general.setIcuNeuroRealTimeBeds(getTagValue("hvcc", eElement));
                    general.setIcuNeuroTotalBeds(getTagValue("hvs11", eElement));
                    general.setIcuThoracicRealTimeBeds(getTagValue("hvccc", eElement));
                    general.setIcuThoracicTotalBeds(getTagValue("hvs16", eElement));
                    general.setCtAvailable(getTagValue("hvctayn", eElement));
                    general.setMriAvailable(getTagValue("hvmariayn", eElement));
                    general.setVentilatorAvailable(getTagValue("hvventiayn", eElement));
                    general.setCrrtAvailable(getTagValue("hvcrrtayn", eElement));
                    general.setEcmoAvailable(getTagValue("hvecmoayn", eElement));
                    general.setAngiographyAvailable(getTagValue("hvangioayn", eElement));

                    generalRepository.save(general);
                }
            }

            // =================================================================
            // 두 번째 API: 중증 질환 정보 파싱 및 업데이트[cite: 1]
            // =================================================================
            Document diseaseDoc = dBuilder.parse(diseaseInfoUrl);
            diseaseDoc.getDocumentElement().normalize();
            NodeList diseaseList = diseaseDoc.getElementsByTagName("item");

            for (int temp = 0; temp < diseaseList.getLength(); temp++) {
                Node nNode = diseaseList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String hpid = getTagValue("hpid", eElement); // 병원 코드 추출[cite: 1]
                    if(hpid == null) continue;

                    // 앞서 1번 API에서 저장해둔 병원 정보를 불러옴
                    General general = generalRepository.findByHpid(hpid).orElse(new General());
                    general.setHpid(hpid);

                    // 중증 질환 정보 추가 셋팅
                    general.setMiAvailable(getTagValue("MKioskTy1", eElement));
                    general.setCerebralInfarctionAvailable(getTagValue("MKioskTy2", eElement));
                    general.setSahSurgeryAvailable(getTagValue("MKioskTy3", eElement));
                    general.setNonSahSurgeryAvailable(getTagValue("MKioskTy4", eElement));
                    general.setGiEndoscopyAvailable(getTagValue("MKioskTy11", eElement));
                    general.setBronchoscopyAvailable(getTagValue("MKioskTy13", eElement));
                    general.setThoracicAortaEmergencyAvailable(getTagValue("MKioskTy5", eElement));
                    general.setAbdominalAortaEmergencyAvailable(getTagValue("MKioskTy6", eElement));
                    general.setEmergencyDialysisCrrtAvailable(getTagValue("MKioskTy23", eElement));
                    general.setPsychiatricClosedWardAvailable(getTagValue("MKioskTy24", eElement));
                    general.setSevereBurnTreatmentAvailable(getTagValue("MKioskTy19", eElement));
                    general.setVascularInterventionAvailable(getTagValue("MKioskTy26", eElement));

                    // 최종 완성된 데이터를 다시 저장(업데이트)
                    generalRepository.save(general);
                }
            }

            System.out.println("공공데이터 파싱 및 병합 DB 저장 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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