package com.itcen.emergencyroad.external.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    HttpSession session = request.getSession(false);

    if ("GET".equalsIgnoreCase(request.getMethod())
        && request.getRequestURI().equals("/mypage")) {
      String referer = request.getHeader("Referer");
      if (referer != null && !referer.contains("/mypage") && !referer.contains("/login")) {
        request.getSession().setAttribute("redirectUrl", referer);
      }
    }

    if (session == null || session.getAttribute("loginUser") == null) {
      String redirectUrl;

      if ("POST".equalsIgnoreCase(request.getMethod())) {
        redirectUrl = request.getHeader("Referer");
        if (redirectUrl == null) {
          redirectUrl = "/home";
        }
      } else {
        redirectUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
          redirectUrl += "?" + queryString;
        }
      }

      request.getSession().setAttribute("redirectUrl", redirectUrl);
      response.sendRedirect("/login");
      return false;
    }

    // /admin/** 경로인 경우 ADMIN role 체크함.
    String requestURI = request.getRequestURI();
    if(requestURI.startsWith("/admin")){
      String role = (String) session.getAttribute("loginRole");
      if(!"ADMIN".equals(role)){
        response.sendRedirect("/"); // ADMIN이 아닌 경우에는 / URL로 보냄(Redirect).
        return false;
      }
    }

    return true;
  }
}
