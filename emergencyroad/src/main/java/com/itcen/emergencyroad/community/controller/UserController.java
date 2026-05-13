package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.LoginRequestDto;
import com.itcen.emergencyroad.community.dto.SignupRequestDto;
import com.itcen.emergencyroad.community.dto.UpdateUserRequestDto;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.service.KakaoService;
import com.itcen.emergencyroad.community.service.ProfileImageService;
import com.itcen.emergencyroad.community.service.UserService;
import com.itcen.emergencyroad.global.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final KakaoService kakaoService;
  private final ProfileImageService profileImageService;

  @GetMapping("/signup")
  public String signupForm(Model model){
    model.addAttribute("signupRequestDto", new SignupRequestDto());

    return "auth/signup";
  }

  @PostMapping("/signup")
  public String signup(@Valid @ModelAttribute SignupRequestDto dto, BindingResult bindingResult,
      @RequestParam(required = false) MultipartFile profileImage, Model model){
    if(bindingResult.hasErrors()){
      return "auth/signup";
    }

    try{
      String profileImageUrl = profileImageService.uploadProfileImage(profileImage);
      dto.setProfileImageUrl(profileImageUrl);

      userService.signUp(dto);
    } catch (CustomException e){
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());
      return "auth/signup";
    }

    return "redirect:/login";
  }

  @GetMapping("/login")
  public String loginForm(Model model){
    model.addAttribute("loginRequestDto", new LoginRequestDto());
    model.addAttribute("kakaoLoginUrl", kakaoService.getKakaoLoginUrl());

    return "auth/login";
  }

  @PostMapping("/login")
  public String login(@Valid @ModelAttribute LoginRequestDto dto, BindingResult bindingResult,
      HttpSession session, Model model){

    if(bindingResult.hasErrors()){
      return "auth/login";
    }

    try{
      userService.login(dto, session);
    }catch (CustomException e){
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());

      return "auth/login";
    }

//    // 추후에 게시판 개발완료 할 때  해당 url로 변경해야 함.
//    return "redirect:/posts";
    String role = (String) session.getAttribute("loginRole");
    if("ADMIN".equals(role)){
      return "redirect:/admin";
    }
    return "redirect:/";
  }

  @GetMapping("/login/kakao")
  public String kakaoLogin(@RequestParam String code, HttpSession session, Model model){

    try {
      userService.kakaoLogin(code, session);
    } catch (CustomException e){
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());

      return "auth/login";
    }

    return "redirect:/posts";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session){
    userService.logout(session);
    return "redirect:/login";
  }

  @GetMapping("/mypage")
  public String mypage(HttpSession session, Model model){
    Long userId = (Long) session.getAttribute("loginUser");
    User user = userService.getUser(userId);
    model.addAttribute("user", user);
    model.addAttribute("updateUserRequestDto", new UpdateUserRequestDto());

    return "auth/mypage";
  }

  @PostMapping("/mypage")
  public String updateUser(HttpSession session, @Valid @ModelAttribute UpdateUserRequestDto dto,
      BindingResult bindingResult, @RequestParam(required = false) MultipartFile profileImage,
      Model model){
    if(bindingResult.hasErrors()){
      Long userId = (Long) session.getAttribute("loginUser");
      model.addAttribute("user", userService.getUser(userId));
      return "auth/mypage";
    }

    try{
      Long userId = (Long) session.getAttribute("loginUser");
      userService.updateUser(userId, dto, profileImage, session);
    }catch (CustomException e){
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());
      Long userId = (Long) session.getAttribute("loginUser");
      model.addAttribute("user", userService.getUser(userId));
      return "auth/mypage";
    }
    return "redirect:/mypage";
  }
}
