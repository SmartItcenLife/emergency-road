package com.itcen.emergencyroad.external.mapper;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.general.entity.GeneralSrsIll;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import org.springframework.stereotype.Component;

//DTO -> Entity
@Component
public class EmrMapper {

    //임산부 Mapper
    public Pregnant toEntity(EmrDto dto, Hospital hospital) {

        if (dto == null) return null;

        return Pregnant.builder()
                .hospital(hospital)
                .hv42(dto.getHv42())
                .hvncc(dto.getHvncc())
                .hv11(dto.getHv11())
                .hvincuayn(dto.getHvincuayn())
                .hvventisoayn(dto.getHvventisoayn())
                .build();
    }
    public void updateEntity(Pregnant entity, EmrDto dto) {
        if (dto == null) return;

        entity.setHv42(dto.getHv42());
        entity.setHvncc(dto.getHvncc());
        entity.setHv11(dto.getHv11());
        entity.setHvincuayn(dto.getHvincuayn());
        entity.setHvventisoayn(dto.getHvventisoayn());
    }

    //TODO
    //소아 Mapper


    //TODO
    //일반 Mapper
    public GeneralRealTimeAndStandard toGeneralEntity(EmrDto dto, Hospital hospital) {

        if (dto == null) return null;

        return GeneralRealTimeAndStandard.builder()
                .hospital(hospital)
                // --- 병상 정보 ---
                .hvec(dto.getHvec())
                .hvs01(dto.getHvs01())
                .hvicc(dto.getHvicc())
                .hvs17(dto.getHvs17())
                .hvcc(dto.getHvcc())
                .hvs11(dto.getHvs11())
                .hvccc(dto.getHvccc())
                .hvs16(dto.getHvs16())
                // --- 응급실 장비 가용 여부 (추가됨) ---
                .hvctayn(dto.getHvctayn())
                .hvmariayn(dto.getHvmariayn())
                .hvventiayn(dto.getHvventiayn())
                .hvcrrtayn(dto.getHvcrrtayn())
                .hvecmoayn(dto.getHvecmoayn())
                .hvangioayn(dto.getHvangioayn())
                .build();
    }

    public void updateGeneralEntity(GeneralRealTimeAndStandard entity, EmrDto dto) {
        if (dto == null) return;
// --- 병상 정보 ---
        entity.setHvec(dto.getHvec());
        entity.setHvs01(dto.getHvs01());
        entity.setHvicc(dto.getHvicc());
        entity.setHvs17(dto.getHvs17());
        entity.setHvcc(dto.getHvcc());
        entity.setHvs11(dto.getHvs11());
        entity.setHvccc(dto.getHvccc());
        entity.setHvs16(dto.getHvs16());

        // --- 응급실 장비 가용 여부 (추가됨) ---
        entity.setHvctayn(dto.getHvctayn());
        entity.setHvmariayn(dto.getHvmariayn());
        entity.setHvventiayn(dto.getHvventiayn());
        entity.setHvcrrtayn(dto.getHvcrrtayn());
        entity.setHvecmoayn(dto.getHvecmoayn());
        entity.setHvangioayn(dto.getHvangioayn());
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




