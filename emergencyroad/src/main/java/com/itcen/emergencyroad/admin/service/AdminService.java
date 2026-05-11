package com.itcen.emergencyroad.admin.service;


import com.itcen.emergencyroad.admin.dto.AdminUserListDTO;
import com.itcen.emergencyroad.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    // 전체 회원 목록 조회
    public List<AdminUserListDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(AdminUserListDTO::new) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }
}
