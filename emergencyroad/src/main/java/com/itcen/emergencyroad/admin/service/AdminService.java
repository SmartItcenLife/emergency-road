package com.itcen.emergencyroad.admin.service;

import com.itcen.emergencyroad.admin.dto.AdminPostListDTO;
import com.itcen.emergencyroad.admin.dto.AdminUserResponseDTO;
import com.itcen.emergencyroad.community.dto.ReportResponseDTO;
import com.itcen.emergencyroad.community.entity.Comment;
import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.Report;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.enums.ReportTargetType;
import com.itcen.emergencyroad.community.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

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
    // AdminService.java 내부
    // 신고받은 게시글, 댓글 관리하기 (상태값 포함)
    public List<ReportResponseDTO> findAllReports(){
        List<Report> reports = reportRepository.findAll();

        return reports.stream().map(report -> {
            boolean isDeleted = false;

            // 대상이 게시글이면 PostRepository에서 삭제 여부 확인
            if (report.getTargetType() == ReportTargetType.POST) {
                isDeleted = postRepository.findById(report.getTargetId())
                        .map(Post::isDeleted)
                        .orElse(true); // 만약 게시글이 아예 DB에 없으면 삭제된 것으로 간주
            }
            // 대상이 댓글이면 CommentRepository에서 삭제 여부 확인
            else if (report.getTargetType() == ReportTargetType.COMMENT) {
                isDeleted = commentRepository.findById(report.getTargetId())
                        .map(Comment::isDeleted)
                        .orElse(true);
            }

            // ⭐ DTO를 만들 때 방금 확인한 isDeleted 값을 같이 넣어서 포장해줍니다!
            return new ReportResponseDTO(report, isDeleted);

        }).collect(Collectors.toList());
    }
    @Transactional
    public void deleteReportedTarget(Long reportId) {
        // 1. 접수된 신고 내역(Report) 찾기
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고 내역을 찾을 수 없습니다."));

        // 2. 신고 대상이 '게시글(POST)'인 경우 -> Post.java의 delete() 호출
        if (report.getTargetType() == ReportTargetType.POST) {
            Post post = postRepository.findById(report.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

            post.delete(); // 상태를 isDeleted = true로 변경하여 숨김 처리!
        }
        // 3. 신고 대상이 '댓글(COMMENT)'인 경우 -> Comment.java의 delete() 호출
        else if (report.getTargetType() == ReportTargetType.COMMENT) {
            Comment comment = commentRepository.findById(report.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

            comment.delete(); // 상태를 isDeleted = true로 변경하여 숨김 처리!
        }

        // 4. 조치가 끝난 '신고 접수증'은 관리자 목록에서 안 보이게 삭제
        reportRepository.delete(report);
    }

}
