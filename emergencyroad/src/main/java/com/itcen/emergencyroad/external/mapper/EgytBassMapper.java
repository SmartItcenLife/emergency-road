package com.itcen.emergencyroad.external.mapper;

import com.itcen.emergencyroad.external.dto.EgytBassDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import org.springframework.stereotype.Component;

@Component
public class EgytBassMapper {

    public HospitalDetail toDetailEntity(EgytBassDto dto, Hospital hospital) {

        if (dto == null) {
            return null;
        }

        return HospitalDetail.builder()
                .hospital(hospital)

                // 병상 및 의료 정보
                .departmentNames(dto.getDgidIdName())
                .hasEmergencyRoom(dto.getDutyEryn())
                
                .emergencyBedCount(dto.getHperyn())
                .availableEmergencyBedCount(dto.getHvec())

                .hpicuCount(dto.getHpicuyn()) //일반 중환자실
                .hpnicuCount(dto.getHpnicuyn()) //신생아 중환자실
                .operatingRoomCount(dto.getHpopyn()) //수술실

                // 진료 시간
                .mondayStartTime(dto.getDutyTime1s())
                .mondayEndTime(dto.getDutyTime1c())

                .tuesdayStartTime(dto.getDutyTime2s())
                .tuesdayEndTime(dto.getDutyTime2c())

                .wednesdayStartTime(dto.getDutyTime3s())
                .wednesdayEndTime(dto.getDutyTime3c())

                .thursdayStartTime(dto.getDutyTime4s())
                .thursdayEndTime(dto.getDutyTime4c())

                .fridayStartTime(dto.getDutyTime5s())
                .fridayEndTime(dto.getDutyTime5c())

                .saturdayStartTime(dto.getDutyTime6s())
                .saturdayEndTime(dto.getDutyTime6c())

                .sundayStartTime(dto.getDutyTime7s())
                .sundayEndTime(dto.getDutyTime7c())

                .build();
    }
    public Hospital toHospitalEntity(EgytBassDto dto) {
        if (dto == null) return null;

        return Hospital.builder()
                .hpid(dto.getHpid())
                .address(dto.getDutyAddr())
                .phone(dto.getDutyTel1())
                .emergencyPhone(dto.getDutyTel3())
                .latitude(dto.getWgs84Lat())
                .longitude(dto.getWgs84Lon())
                .hasEmergency(dto.getDutyEryn())
                .build();
    }


}