package com.itcen.emergencyroad.external.mapper;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.Pregnant; // 예시 엔티티
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



}




