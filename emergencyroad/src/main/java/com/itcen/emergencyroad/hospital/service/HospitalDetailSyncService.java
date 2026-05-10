//package com.itcen.emergencyroad.hospital.service;
//
//import com.itcen.emergencyroad.external.dto.EgytBassDto;
//import com.itcen.emergencyroad.external.mapper.EgytBassMapper;
//import com.itcen.emergencyroad.hospital.entity.Hospital;
//import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
//import com.itcen.emergencyroad.hospital.repository.HospitalDetailRepository;
//import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//
////최적화 전 -> 일단 이것은 필요 없는 클래스
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class HospitalDetailSyncService {
//
//    private final HospitalDetailRepository hospitalDetailRepository;
//    private final HospitalRepository hospitalRepository;
//    private final EgytBassMapper mapper;
//
//
//    @Transactional
//    public void saveOrUpdate(List<EgytBassDto> list) {
//
//        for (EgytBassDto dto : list) {
//
//            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid())
//                    .orElse(null);
//
//            log.info("hospital exists = {}", hospital != null);
//            if (hospital == null) continue;
//
//            HospitalDetail hospitalDetail =
//                    hospitalDetailRepository.findByHospital(hospital)
//                            .orElse(null);
//            // 신규 저장
//            if (hospitalDetail == null) {
//                hospitalDetail =
//                        mapper.toDetailEntity(dto, hospital);
//            } else {
//                // 기존 데이터 업데이트
//                hospitalDetail.updateDetail(dto);
//            }
//            hospitalDetailRepository.save(hospitalDetail);
//
//        }
//    }
//}
