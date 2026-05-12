package com.itcen.emergencyroad.community.dto;

import com.itcen.emergencyroad.community.entity.Report;
import com.itcen.emergencyroad.community.enums.ReportTargetType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReportResponseDTO {
    private Long id;
    private String reporterNickname;
    private ReportTargetType targetType;
    private Long targetId;
    private LocalDateTime createdAt;

    public ReportResponseDTO(Report report) {
        this.id = report.getId();
        this.reporterNickname = report.getReporter().getNickname();
        this.targetType = report.getTargetType();
        this.targetId = report.getTargetId();
        this.createdAt = report.getCreatedAt();
    }
}
