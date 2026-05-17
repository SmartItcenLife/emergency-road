package com.itcen.emergencyroad.external.mapper;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.general.entity.GeneralSrsIll;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeMkiosktyDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricStandardDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricMkioskty;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//DTO -> Entity
@Component
public class EmrMapper {

    //임산부 Mapper
    public Pregnant toPregnantEntity(EmrDto dto, Hospital hospital) {

        if (dto == null) return null;

        return Pregnant.builder()
                .hospital(hospital)
                .nicuAvailable(dto.getMKioskTy15())
                .deliveryAvailable(dto.getMKioskTy16())
                .obstetricSurgeryAvailable(dto.getMKioskTy17())
                .gynecologySurgeryAvailable(dto.getMKioskTy18())
                .emergencyDialysisAvailable(dto.getMKioskTy22())
                .build();
    }

    public PregnantRealtime toPregnantRealTimeStatusEntity(EmrDto dto, Hospital hospital) {

        if (dto == null) return null;

        return PregnantRealtime.builder()
                .hospital(hospital)
                .incubatorExists(dto.getHv11())
                .isDeliveryRoomAvailable(dto.getHv42())
                .nicuBedCount(dto.getHvncc())
                .incubatorAvailable(dto.getHvincuayn())
                .prematureVentilatorAvailable(dto.getHvventisoayn())
                .build();
    }
    public PregnantStandard toPregnantStandardStatusEntity(EmrDto dto, Hospital hospital) {

        if (dto == null) return null;

        return PregnantStandard.builder()
                .hospital(hospital)
                .deliveryRoomStandard(dto.getHvs26())
                .nicuStandard(dto.getHvs08())
                .ventilatorStandard(dto.getHvs31())
                .incubatorStandard(dto.getHvs32())
                .build();
    }


    //소아 Mapper
    // 소아 realtime Mapper
    public PediatricRealtime toPediatricEntity(PediatricRealtimeDto dto, Hospital hospital) {
        if (dto == null) return null;

        return PediatricRealtime.builder()
                .hospital(hospital)
                .pediatricVentiAvailable(dto.getHv10())                         // 소아 인공 호흡기 가능 여부
                .preemieVentiAvailable(dto.getHvventisoayn())                   // 조산아용 인공호흡기 가능 여부
                .incubatorAvailable(dto.getHv11())                          // 인큐베이터(보육기) 가능 여부
                .incubatorResourceAvailable(dto.getHvincuayn())              // 인큐베이터 가용 여부
                .pediatricNegativeIsolationCount(parseInt(dto.getHv15()))     // 소아 음압격리
                .pediatricGeneralIsolationCount(parseInt(dto.getHv16()))     // 소아 일반 격리
                .pediatricBedCount(parseInt(dto.getHv28()))                  // 소아 현황
                .pediatricIcuCount(parseInt(dto.getHv32()))                  // [중환자실] 소아
                .pediatricEmergencyIcuCount(parseInt(dto.getHv33()))         // [응급]소아 중환자실
                .pediatricEmergencyAdmissionCount(parseInt(dto.getHv37()))  // [응급] 소아 입원실
                .pediatricHotline(clean(dto.getHv12()))                      // 소아당직의 직통 연락처
                .recordedAt(parseDateTime(dto.getHvidate()))                    // 입력일시
                .build();
    }

    public void updatePediatricEntity(PediatricRealtime entity, PediatricRealtimeDto dto) {
        if (dto == null || entity == null) return;

        entity.updateRealtimeData(
                dto.getHv10(),
                dto.getHvventisoayn(),
                dto.getHv11(),
                dto.getHvincuayn(),
                parseInt(dto.getHv15()),
                parseInt(dto.getHv16()),
                parseInt(dto.getHv28()),
                parseInt(dto.getHv32()),
                parseInt(dto.getHv33()),
                parseInt(dto.getHv37()),
                clean(dto.getHv12()),
                parseDateTime(dto.getHvidate())
        );
    }
    // 소아 mkioskty Mapper
    public PediatricMkioskty toPediatricMkiosktyEntity(PediatricRealtimeMkiosktyDto dto, Hospital hospital) {
        if (dto == null) return null;

        return PediatricMkioskty.builder()
                .hospital(hospital)
                .pediatricBowelObstructionAvailable(clean(dto.getMkioskty10()))                // 장중첩/폐색 영유아
                .pediatricEmergencyEndoscopyGastroAvailable(clean(dto.getMkioskty12()))        // 응급내시경 영유아 위장관
                .pediatricEmergencyEndoscopyBronchialAvailable(clean(dto.getMkioskty14()))     // 응급내시경 영유아 기관지
                .lowBirthWeightInfantAvailable(clean(dto.getMkioskty15()))                     // 저체중출생아
                .pediatricVascularInterventionAvailable(clean(dto.getMkioskty27()))            // 영상의학혈관중재 영유아
                .pediatricBowelObstructionMessage(clean(dto.getMkioskty10Msg()))
                .pediatricEmergencyEndoscopyGastroMessage(clean(dto.getMkioskty12Msg()))
                .pediatricEmergencyEndoscopyBronchialMessage(clean(dto.getMkioskty14Msg()))
                .lowBirthWeightInfantMessage(clean(dto.getMkioskty15Msg()))
                .pediatricVascularInterventionMessage(clean(dto.getMkioskty27Msg()))
                .build();
    }

    public void updatePediatricMkiosktyEntity(PediatricMkioskty entity, PediatricRealtimeMkiosktyDto dto) {
        if (dto == null || entity == null) return;

        entity.update(
                clean(dto.getMkioskty10()),
                clean(dto.getMkioskty12()),
                clean(dto.getMkioskty14()),
                clean(dto.getMkioskty15()),
                clean(dto.getMkioskty27()),
                clean(dto.getMkioskty10Msg()),
                clean(dto.getMkioskty12Msg()),
                clean(dto.getMkioskty14Msg()),
                clean(dto.getMkioskty15Msg()),
                clean(dto.getMkioskty27Msg())
        );
    }

    // 소아 standard Mapper
    public PediatricStandard toPediatricStandardEntity(PediatricStandardDto dto, Hospital hospital) {
        if (dto == null) return null;

        return PediatricStandard.builder()
                .hospital(hospital)
                .pediatricBedStandard(parseInt(dto.getHvs02()))                  // 소아 병상수 기준
                .newbornIcuStandard(parseInt(dto.getHvs08()))                   // 신생아 중환자실 기준
                .pediatricIcuStandard(parseInt(dto.getHvs09()))                 // 소아 중환자실 기준
                .pediatricEmergencyIcuStandard(parseInt(dto.getHvs10()))        // 응급전용 소아 중환자실 기준
                .pediatricEmergencyAdmissionStandard(parseInt(dto.getHvs20()))  // 응급전용 소아 입원실 기준
                .generalPediatricVentiStandard(parseInt(dto.getHvs30()))        // 인공호흡기 일반소아 기준
                .preemieVentiStandard(parseInt(dto.getHvs31()))                 // 인공호흡기 조산아 기준
                .incubatorStandard(parseInt(dto.getHvs32()))                    // 인큐베이터 기준
                .pediatricNegativeIsolationStandard(parseInt(dto.getHvs48()))   // 소아 음압격리실 기준
                .pediatricGeneralIsolationStandard(parseInt(dto.getHvs49()))    // 소아 일반격리실 기준
                .recordedAt(parseDateTime(dto.getHvidate()))                    // 입력일시
                .build();
    }

    public void updatePediatricStandardEntity(PediatricStandard entity, PediatricStandardDto dto) {
        if (dto == null || entity == null) return;

        entity.updateStandardData(
                parseInt(dto.getHvs02()),
                parseInt(dto.getHvs08()),
                parseInt(dto.getHvs09()),
                parseInt(dto.getHvs10()),
                parseInt(dto.getHvs20()),
                parseInt(dto.getHvs30()),
                parseInt(dto.getHvs31()),
                parseInt(dto.getHvs32()),
                parseInt(dto.getHvs48()),
                parseInt(dto.getHvs49()),
                parseDateTime(dto.getHvidate())
        );
    }



    private Integer parseInt(String value) {
        try {
            if (value == null || value.isBlank()) return null;
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            if (value == null || value.isBlank()) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return LocalDateTime.parse(value.trim(), formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }


    //TODO
    //일반 Mapper
    public GeneralRealTimeAndStandard toGeneralEntity(EmrDto dto, Hospital hospital) {

        if (dto == null) return null;

        return GeneralRealTimeAndStandard.builder()
                .hospital(hospital)
                // --- 병상 정보 ---
                .emergencyAvailableBeds(dto.getHvec())
                .emergencyTotalBeds(dto.getHvs01())

                .icuAvailableBeds(dto.getHvicc())
                .icuTotalBeds(dto.getHvs17())

                .neuroIcuAvailableBeds(dto.getHvcc())
                .neuroIcuTotalBeds(dto.getHvs11())

                .chestIcuAvailableBeds(dto.getHvccc())
                .chestIcuTotalBeds(dto.getHvs16())

                .ctAvailable(dto.getHvctayn())
                .mriAvailable(dto.getHvmariayn())
                .ventilatorAvailable(dto.getHvventiayn())
                .crrtAvailable(dto.getHvcrrtayn())
                .ecmoAvailable(dto.getHvecmoayn())
                .angioAvailable(dto.getHvangioayn())
                .build();
    }

    public void updateGeneralEntity(GeneralRealTimeAndStandard entity, EmrDto dto) {
        if (dto == null) return;
// --- 병상 정보 ---
        entity.setEmergencyAvailableBeds(dto.getHvec());
        entity.setEmergencyTotalBeds(dto.getHvs01());

        entity.setIcuAvailableBeds(dto.getHvicc());
        entity.setIcuTotalBeds(dto.getHvs17());

        entity.setNeuroIcuAvailableBeds(dto.getHvcc());
        entity.setNeuroIcuTotalBeds(dto.getHvs11());

        entity.setChestIcuAvailableBeds(dto.getHvccc());
        entity.setChestIcuTotalBeds(dto.getHvs16());

        entity.setCtAvailable(dto.getHvctayn());
        entity.setMriAvailable(dto.getHvmariayn());
        entity.setVentilatorAvailable(dto.getHvventiayn());
        entity.setCrrtAvailable(dto.getHvcrrtayn());
        entity.setEcmoAvailable(dto.getHvecmoayn());
        entity.setAngioAvailable(dto.getHvangioayn());
    }

    // TODO
    // 중증질환 수용 가능 여부
    public GeneralSrsIll toSrsillEntity(EmrDto dto, Hospital hospital) {
        if (dto == null) return null;

        return GeneralSrsIll.builder()
                .hospital(hospital)
                // --- 일반 ---
                .MKioskTy1(dto.getMKioskTy1())
                .MKioskTy2(dto.getMKioskTy2())
                .MKioskTy3(dto.getMKioskTy3())
                .MKioskTy4(dto.getMKioskTy4())
                .MKioskTy5(dto.getMKioskTy5())
                .MKioskTy6(dto.getMKioskTy6())
                .MKioskTy23(dto.getMKioskTy23())
                .MKioskTy24(dto.getMKioskTy24())
                .MKioskTy11(dto.getMKioskTy11())
                .MKioskTy13(dto.getMKioskTy13())
                .MKioskTy19(dto.getMKioskTy19())
                .MKioskTy26(dto.getMKioskTy26())
                // --- 임산부 ---
                //.MKioskTy2(dto.getMKioskTy22())
                //.MKioskTy16(dto.getMKioskTy16())
                //.MKioskTy17(dto.getMKioskTy17())
                //.MKioskTy18(dto.getMKioskTy18())
                // --- 소아 ---
//                .MKioskTy10(dto.getMKioskTy10())
//                .MKioskTy12(dto.getMKioskTy12())
//                .MKioskTy14(dto.getMKioskTy14())
//                .MKioskTy15(dto.getMKioskTy15())
//                .MKioskTy27(dto.getMKioskTy27())
                .build();
    }

    public void updateSrsillData(GeneralSrsIll entity, EmrDto dto) {
        if(dto==null || entity == null) return;

        // 일반
        entity.setMKioskTy1(dto.getMKioskTy1());
        entity.setMKioskTy2(dto.getMKioskTy2());
        entity.setMKioskTy3(dto.getMKioskTy3());
        entity.setMKioskTy4(dto.getMKioskTy4());
        entity.setMKioskTy5(dto.getMKioskTy5());
        entity.setMKioskTy6(dto.getMKioskTy6());
        entity.setMKioskTy23(dto.getMKioskTy23());
        entity.setMKioskTy24(dto.getMKioskTy24());
        entity.setMKioskTy11(dto.getMKioskTy11());
        entity.setMKioskTy13(dto.getMKioskTy13());
        entity.setMKioskTy19(dto.getMKioskTy19());
        entity.setMKioskTy26(dto.getMKioskTy26());

//        // 임산부
//        entity.setMKioskTy22(dto.getMKioskTy22());
//        entity.setMKioskTy16(dto.getMKioskTy16());
//        entity.setMKioskTy17(dto.getMKioskTy17());
//        entity.setMKioskTy18(dto.getMKioskTy18());
//
//        // 소아
//        entity.setMKioskTy10(dto.getMKioskTy10());
//        entity.setMKioskTy12(dto.getMKioskTy12());
//        entity.setMKioskTy14(dto.getMKioskTy14());
//        entity.setMKioskTy15(dto.getMKioskTy15());
//        entity.setMKioskTy27(dto.getMKioskTy27());
    }

}




