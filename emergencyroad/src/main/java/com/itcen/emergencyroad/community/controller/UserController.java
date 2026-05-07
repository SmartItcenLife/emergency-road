package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.LoginRequestDto;
import com.itcen.emergencyroad.community.dto.SignupRequestDto;
import com.itcen.emergencyroad.community.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/signup")
  public String signupForm(Model model){
    model.addAttribute("signupRequestDto", new SignupRequestDto());

    return "auth/signup";
  }

  @PostMapping("/signup")
  public String signup(@Valid @ModelAttribute SignupRequestDto dto, BindingResult bindingResult, Model model){
    if(bindingResult.hasErrors()){
      return "auth/signup";
    }

    try{
      userService.signUp(dto);
    } catch (IllegalArgumentException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "auth/signup";
    }

    return "redirect:/login";
  }

  @GetMapping("/login")
  public String loginForm(Model model){
    model.addAttribute("loginRequestDto", new LoginRequestDto());

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
    }catch (IllegalArgumentException e){
      model.addAttribute("errorMessage", e.getMessage());

      return "auth/login";
    }

    // 추후에 게시판 개발완료 할 때  해당 url로 변경해야 함.
    return "redirect:/posts";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session){
    session.invalidate();
    return "redirect:/login";
  }
}
