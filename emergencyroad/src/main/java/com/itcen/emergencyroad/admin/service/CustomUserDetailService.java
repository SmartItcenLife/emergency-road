package com.itcen.emergencyroad.admin.service;

import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName){
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userName));

        // 스프링 시큐리티에서 인식하는 User 객체로 변환하여 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole().name()) // Role enum의 이름(ADMIN, USER 등)을 전달
                .build();
    }
    }
