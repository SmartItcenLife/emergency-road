//package com.itcen.emergencyroad.external.mapper;
//
//import com.itcen.emergencyroad.general.dto.GeneralSrsIllDTO;
//import com.itcen.emergencyroad.general.entity.GeneralSrsIll;
//import com.itcen.emergencyroad.hospital.entity.Hospital;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SrsIllMapper {
//    public GeneralSrsIll toGenSrsEntity(GeneralSrsIllDTO dto, Hospital hospital){
//        if(dto==null) return null;
//
//        return GeneralSrsIll.builder()
//                .hospital(hospital)
//                .MKioskTy1(dto.getMKioskTy1())
//                .MKioskTy2(dto.getMKioskTy2())
//                .MKioskTy3(dto.getMKioskTy3())
//                .MKioskTy4(dto.getMKioskTy4())
//                .MKioskTy5(dto.getMKioskTy5())
//                .MKioskTy6(dto.getMKioskTy6())
//                .MKioskTy23(dto.getMKioskTy23())
//                .MKioskTy24(dto.getMKioskTy24())
//                .MKioskTy11(dto.getMKioskTy11())
//                .MKioskTy13(dto.getMKioskTy13())
//                .MKioskTy19(dto.getMKioskTy19())
//                .MKioskTy26(dto.getMKioskTy26())
//                .build();
//    }
//    public void updateGenSrsEntity(GeneralSrsIll entity, GeneralSrsIllDTO dto){
//        if(dto==null) return;
//
//        entity.setMKioskTy1(dto.getMKioskTy1());
//        entity.setMKioskTy2(dto.getMKioskTy2());
//        entity.setMKioskTy3(dto.getMKioskTy3());
//        entity.setMKioskTy4(dto.getMKioskTy4());
//        entity.setMKioskTy5(dto.getMKioskTy5());
//        entity.setMKioskTy6(dto.getMKioskTy6());
//        entity.setMKioskTy23(dto.getMKioskTy23());
//        entity.setMKioskTy24(dto.getMKioskTy24());
//        entity.setMKioskTy11(dto.getMKioskTy11());
//        entity.setMKioskTy13(dto.getMKioskTy13());
//        entity.setMKioskTy19(dto.getMKioskTy19());
//        entity.setMKioskTy26(dto.getMKioskTy26());
//    }
//}
