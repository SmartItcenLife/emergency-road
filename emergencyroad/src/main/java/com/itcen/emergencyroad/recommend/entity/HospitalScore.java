package com.itcen.emergencyroad.recommend.entity;

import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 스케줄러에 의해 80초마다 갱신되는 병원별 추천 점수 관리 엔티티
 * 사용자의 거리 데이터와 결합하여 최종 추천 순위를 결정
 */
@Entity
@Table(name = "hospital_score")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HospitalScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", unique = true, nullable = false)
    private Hospital hospital;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private HospitalDetail hospitalDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PediatricStandard pediatricStandard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PediatricRealtime pediatricRealtime;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "hpid",
            referencedColumnName = "hpid",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Pregnant pregnant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PregnantStandard pregnantStandard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PregnantRealtime pregnantRealtime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private GeneralRealTimeAndStandard generalRealTimeAndStandard;

    // --- 카테고리별 추천 점수 (0.0 ~ 100.0) ---
    @Builder.Default
    @Column(name = "pregnant_score")
    private Double pregnantScore = 0.0; // 임산부 응급 점수 (분만실, 산과수술 등 반영)

    @Builder.Default
    @Column(name = "pediatric_score")
    private Double pediatricScore = 0.0; // 소아 응급 점수 (소아과 전문의, NICU 등 반영)

    @Builder.Default
    @Column(name = "general_score")
    private Double generalScore = 0.0;   // 일반 응급 점수 (가용 병상, 응급실 가동률 반영)

    // --- 화면 노출용 상태 요약 정보 (반정규화) ---

    @Column(name = "pregnant_tag", length = 100)
    private String pregnantTags; // 예: "분만 가능 | NICU 보유"

    @Column(name = "pediatric_tag", length = 100)
    private String pediatricTags; // 예: "소아 전문의 상주"

    @Column(name = "general_tag", length = 100)
    private String generalTags;   // 예: "응급실 여유"

    // --- 데이터 신뢰도 관리 ---

    @Column(name = "last_calculated_at")
    private LocalDateTime lastCalculatedAt; // 마지막으로 점수가 계산된 시각


    /**
     * 임산부 점수 및 태그 업데이트 로직
     */
    public void updatePregnantScore(Double score, String tag) {
        this.pregnantScore = score;
        this.pregnantTags = tag;
        this.lastCalculatedAt = LocalDateTime.now();
    }

    /**
     * 소아 점수 및 태그 업데이트 로직
     */
    public void updatePediatricScore(Double score, String tag) {
        this.pediatricScore = score;
        this.pediatricTags = tag;
        this.lastCalculatedAt = LocalDateTime.now();
    }

    /**
     * 일반 응급 점수 및 태그 업데이트 로직
     */
    public void updateGeneralScore(Double score, String tag) {
        this.generalScore = score;
        this.generalTags = tag;
        this.lastCalculatedAt = LocalDateTime.now();
    }
}