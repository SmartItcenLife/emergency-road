package com.itcen.emergencyroad.external.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoginInterceptor())
        .addPathPatterns("/hospitals/*/posts/new",
            "/hospitals/*/posts",               // 게시글 작성 처리 (POST)
            "/hospitals/*/posts/*/edit",        // 게시글 수정 화면
            "/hospitals/*/posts/*/delete",      // 게시글 삭제
            "/admin/**"
        );
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:uploads/");
  }
}
