package com.itcen.emergencyroad.admin.service;

import com.itcen.emergencyroad.admin.dto.AdminPostListDTO;
import com.itcen.emergencyroad.admin.dto.AdminUserResponseDTO;
import com.itcen.emergencyroad.community.dto.ReportResponseDTO;
import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.Report;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.enums.ReportTargetType;
import com.itcen.emergencyroad.community.repository.ReportRepository;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.community.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    // 전체 회원 목록 조회
    public List<AdminUserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(AdminUserResponseDTO::new) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }
    // 사용자 삭제하기
    @Transactional // 데이터 베이스의 데이터를 변경(생성, 수정, 삭제)할 때 안전장치
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    // 관리자용 전체 게시글 목록 조회(최신순)
    public List<AdminPostListDTO> findAllPosts(){
        return postRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(AdminPostListDTO::new)
                .collect(Collectors.toList());
    }
    // 게시글 삭제하기
    @Transactional
    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                        .orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        post.delete();
    }

    // 신고받은 게시글, 댓글 관리하기
    public List<ReportResponseDTO> findAllReports(){
        List<Report> reports = reportRepository.findAll();

        return reports.stream()
                .map(ReportResponseDTO::new)
                .toList();
    }
//    // 신고받은 게시글, 댓글 DB에 저장하기
//    @Transactional
//    public void saveReports(Long id){
//        Report report = reportRepository.findById(id)
//                .orElseThrow(()->new IllegalArgumentException("해당 게시글/댓글이 존재하지 않습니다"));
//        report.saveReport(User reporter, ReportTargetType targetType, Long targetId);
//    }

}
