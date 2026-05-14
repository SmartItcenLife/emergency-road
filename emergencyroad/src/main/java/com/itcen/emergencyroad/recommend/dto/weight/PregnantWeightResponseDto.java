package com.itcen.emergencyroad.recommend.dto.weight;

import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import lombok.Getter;

@Getter
public class PregnantWeightResponseDto {
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

    public PregnantWeightResponseDto(WeightPregnantConfiguration entity){
        this.deliveryAvailableWeight = entity.getDeliveryAvailableWeight();
        this.obstetricSurgeryWeight = entity.getObstetricSurgeryWeight();
        this.nicuAvailableWeight = entity.getNicuAvailableWeight();
        this.deliveryRoomAvailableWeight = entity.getDeliveryRoomAvailableWeight();
        this.emergencyRoomAvailableWeight = entity.getEmergencyRoomAvailableWeight();
        this.operatingRoomThreshold = entity.getOperatingRoomThreshold();
        this.incubatorWeight = entity.getIncubatorWeight();
        this.prematureVentilatorWeight = entity.getPrematureVentilatorWeight();
        this.operatingRoomBonusWeight = entity.getOperatingRoomBonusWeight();
        this.nicuScaleWeight = entity.getNicuScaleWeight();
        this.maxNicuScaleScore = entity.getMaxNicuScaleScore();
    }

}
