package com.itcen.emergencyroad.recommend.dto.weight;

import com.itcen.emergencyroad.recommend.entity.WeightGeneralConfiguration;
import lombok.Getter;

@Getter
public class GeneralWeightResponseDto {
    private Double emergencyRoomWeight;
    private Double severeDiseaseWeight;
    private Double icuWeight;
    private Double equipmentWeight;
    private Double congestionWeight;

    private Double ecmoBonus;
    private Double crrtBonus;
    private Double angioBonus;

    public GeneralWeightResponseDto(WeightGeneralConfiguration entity){
        this.emergencyRoomWeight = entity.getEmergencyRoomWeight();
        this.severeDiseaseWeight = entity.getSevereDiseaseWeight();
        this.icuWeight = entity.getIcuWeight();
        this.equipmentWeight = entity.getEquipmentWeight();
        this.congestionWeight = entity.getCongestionWeight();
        this.ecmoBonus = entity.getEcmoBonus();
        this.crrtBonus = entity.getCrrtBonus();
        this.angioBonus = entity.getAngioBonus();
    }

}
