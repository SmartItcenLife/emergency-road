package com.itcen.emergencyroad.community.entity;

import com.itcen.emergencyroad.community.enums.ReportTargetType;
import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name="reports")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="report_id")
    private Long id;

    @Column(name="hpid")
    private String hpid;

    // 신고자
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="reporter_id", nullable = false)
    private User reporter;

    // 신고당한 것의 유형
    @Enumerated(EnumType.STRING)
    @Column(name="target_type", nullable = false)
    private ReportTargetType targetType;

    // 신고당한 게시글이나 댓글의 번호
    @Column(name="target_id", nullable = false)
    private Long targetId;

    @Column(name="reason", length = 255)
    private String reason;

    // 신고 생성 메서드
    public static Report createReport(User reporter,  ReportTargetType targetType, Long targetId, String reason, String hpid){
        Report report = new Report();
        report.reporter = reporter;
        report.targetType = targetType;
        report.targetId = targetId;
        report.reason = reason;
        report.hpid = hpid;
        return report;
    }

}
