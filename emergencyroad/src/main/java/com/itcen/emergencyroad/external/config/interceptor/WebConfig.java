package com.itcen.emergencyroad.external.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    // 현재 URL 저장용 인터셉터 (모든 경로)
    registry.addInterceptor(new HandlerInterceptor() {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("currentUrl", request.getRequestURI());
        return true;
      }
    });

    registry.addInterceptor(new LoginInterceptor())
        .addPathPatterns(
            // 게시글
            "/hospitals/*/posts/new",
            "/hospitals/*/posts/*/edit",        // 게시글 수정 화면
            "/hospitals/*/posts/*/delete",      // 게시글 삭제
            "/hospitals/*/posts/*/report",       // 게시글 신고
            // 댓글
            "/hospitals/*/posts/*/comments",         // 댓글 작성
            "/hospitals/*/posts/*/comments/*/edit",  // 댓글 수정
            "/hospitals/*/posts/*/comments/*/delete",// 댓글 삭제
            "/hospitals/*/posts/*/comments/*/report",// 댓글 신고
            // 좋아요
            "/hospitals/*/posts/*/like",             // 게시글 좋아요
            "/hospitals/*/posts/*/comments/*/like",  // 댓글 좋아요
            // 관리자
            "/admin/**",
            // 마이페이지
            "/mypage"
        );
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:uploads/");
  }
}
