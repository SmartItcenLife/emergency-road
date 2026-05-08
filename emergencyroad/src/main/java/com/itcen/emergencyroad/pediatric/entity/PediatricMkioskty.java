package com.itcen.emergencyroad.pediatric.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pediatric_mkioskty")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PediatricMkioskty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @Column(name = "mkioskty10")
    private String pediatricBowelObstructionAvailable; // 장중첩/폐색 영유아

    @Column(name = "mkioskty12")
    private String pediatricEmergencyEndoscopyGastroAvailable; // 응급내시경 영유아 위장관

    @Column(name = "mkioskty14")
    private String pediatricEmergencyEndoscopyBronchialAvailable; // 응급내시경 영유아 기관지

    @Column(name = "mkioskty15")
    private String lowBirthWeightInfantAvailable; // 저체중출생아

    @Column(name = "mkioskty27")
    private String pediatricVascularInterventionAvailable; // 영상의학혈관중재 영유아

    @Column(name = "mkioskty10_msg", length = 255)
    private String pediatricBowelObstructionMessage;

    @Column(name = "mkioskty12_msg", length = 255)
    private String pediatricEmergencyEndoscopyGastroMessage;

    @Column(name = "mkioskty14_msg", length = 255)
    private String pediatricEmergencyEndoscopyBronchialMessage;

    @Column(name = "mkioskty15_msg", length = 255)
    private String lowBirthWeightInfantMessage;

    @Column(name = "mkioskty27_msg", length = 255)
    private String pediatricVascularInterventionMessage;

    public void update(
            String pediatricBowelObstructionAvailable,
            String pediatricEmergencyEndoscopyGastroAvailable,
            String pediatricEmergencyEndoscopyBronchialAvailable,
            String lowBirthWeightInfantAvailable,
            String pediatricVascularInterventionAvailable,
            String pediatricBowelObstructionMessage,
            String pediatricEmergencyEndoscopyGastroMessage,
            String pediatricEmergencyEndoscopyBronchialMessage,
            String lowBirthWeightInfantMessage,
            String pediatricVascularInterventionMessage
    ) {
        this.pediatricBowelObstructionAvailable = pediatricBowelObstructionAvailable;
        this.pediatricEmergencyEndoscopyGastroAvailable = pediatricEmergencyEndoscopyGastroAvailable;
        this.pediatricEmergencyEndoscopyBronchialAvailable = pediatricEmergencyEndoscopyBronchialAvailable;
        this.lowBirthWeightInfantAvailable = lowBirthWeightInfantAvailable;
        this.pediatricVascularInterventionAvailable = pediatricVascularInterventionAvailable;
        this.pediatricBowelObstructionMessage = pediatricBowelObstructionMessage;
        this.pediatricEmergencyEndoscopyGastroMessage = pediatricEmergencyEndoscopyGastroMessage;
        this.pediatricEmergencyEndoscopyBronchialMessage = pediatricEmergencyEndoscopyBronchialMessage;
        this.lowBirthWeightInfantMessage = lowBirthWeightInfantMessage;
        this.pediatricVascularInterventionMessage = pediatricVascularInterventionMessage;
    }
}