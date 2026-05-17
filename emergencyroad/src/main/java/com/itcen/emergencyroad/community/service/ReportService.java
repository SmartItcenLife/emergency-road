package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.entity.Report;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.ReportRepository;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.community.enums.ReportTargetType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // 신고받은 게시글, 댓글 DB에 저장하기
    @Transactional
    public void createReport(Long reporterId, ReportTargetType targetType, Long targetId, String reason, String hpid){
        boolean alreadyReported = reportRepository.existsByReporterIdAndTargetTypeAndTargetId(reporterId, targetType, targetId);
        if(alreadyReported){
            throw new IllegalArgumentException("이미 신고가 접수된 항목입니다.");
        }

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(()->new IllegalArgumentException("해당 유저가 존재하지 않습니다"));

        Report newReport = Report.createReport(reporter, targetType, targetId, reason, hpid); // 신고자 이름, 신고 대상을 적음

        reportRepository.save(newReport); // jpa가 제공하는 메서드
    }
}
