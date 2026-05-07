package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.SignupRequestDto;
import com.itcen.emergencyroad.community.service.UserService;
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



}
