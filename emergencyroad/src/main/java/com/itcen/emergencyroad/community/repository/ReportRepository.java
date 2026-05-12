package com.itcen.emergencyroad.community.repository;
import com.itcen.emergencyroad.community.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReporterIdAndTargetTypeAndTargetId(Long reporterId, com.itcen.emergencyroad.community.enums.ReportTargetType targetType, Long targetId);
}
