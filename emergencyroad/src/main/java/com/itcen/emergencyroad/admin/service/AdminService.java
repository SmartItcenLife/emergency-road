package com.itcen.emergencyroad.admin.service;

import com.itcen.emergencyroad.admin.dto.AdminPostListDTO;
import com.itcen.emergencyroad.admin.dto.AdminUserResponseDTO;
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

    // 전체 회원 목록 조회
    public List<AdminUserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(AdminUserResponseDTO::new) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    // 관리자용 전체 게시글 목록 조회(최신순)
    public List<AdminPostListDTO> findAllPosts(){
        return postRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(AdminPostListDTO::new)
                .collect(Collectors.toList());
    }

    // 관리자
}
