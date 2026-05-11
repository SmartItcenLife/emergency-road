//package com.itcen.emergencyroad.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // 테스트를 위해 CSRF는 잠시 꺼둡니다.
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin으로 시작하는 모든 경로는 ADMIN 권한 필수
//                        .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**").permitAll() // 누구나 접근 가능
//                        .anyRequest().authenticated() // 그 외 모든 요청은 로그인이 필요함
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // 로그인 페이지 주소
//                        .defaultSuccessUrl("/") // 로그인 성공 시 이동할 주소
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 주소
//                        .invalidateHttpSession(true) // 세션 무효화
//                );
//
//        return http.build();
//    }
//
//    // 비밀번호 암호화를 위한 빈 등록
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
