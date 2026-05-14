package com.itcen.emergencyroad.recommend.dto.weight;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PregnantWeightRequestDto {
    private Double deliveryAvailableWeight;
    private Double obstetricSurgeryWeight;
    private Double nicuAvailableWeight;
    private Double deliveryRoomAvailableWeight;
    private Double emergencyRoomAvailableWeight;
    private Integer operatingRoomThreshold;
    private Double incubatorWeight;
    private Double prematureVentilatorWeight;
    private Double operatingRoomBonusWeight;
    private Double nicuScaleWeight;
    private Double maxNicuScaleScore;

}
