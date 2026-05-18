package com.itcen.emergencyroad.recommend.dto.weight;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralWeightRequestDto {
    private Double emergencyRoomWeight;
    private Double severeDiseaseWeight;
    private Double icuWeight;
    private Double equipmentWeight;
    private Double congestionWeight;

    private Double ecmoBonus;
    private Double crrtBonus;
    private Double angioBonus;
}
