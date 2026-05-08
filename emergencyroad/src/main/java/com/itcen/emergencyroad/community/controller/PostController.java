package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.post.PostRequestDto;
import com.itcen.emergencyroad.community.dto.post.PostResponseDto;
import com.itcen.emergencyroad.community.service.PostService;
import com.itcen.emergencyroad.global.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hospitals/{hpid}/posts")
public class PostController {

  private final PostService postService;

  @GetMapping
  public String getPosts(@PathVariable String hpid,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) String keyword,
      Model model) {
    Page<PostResponseDto> posts = postService.getPosts(hpid, page, keyword);

    model.addAttribute("posts", posts);
    model.addAttribute("hpid", hpid);
    model.addAttribute("keyword", keyword);
    return "community/post-list";
  }

  @GetMapping("/{postId}")
  public String getPost(@PathVariable String hpid,
      @PathVariable Long postId,
      Model model,
      HttpSession session) {
    PostResponseDto post = postService.getPost(postId);

    model.addAttribute("post", post);
    model.addAttribute("hpid", hpid);
    model.addAttribute("loginUser", session.getAttribute("loginUser"));
    return "community/post-deail";
  }

  @PostMapping
  public String createPost(@PathVariable String hpid,
      @Valid @ModelAttribute PostRequestDto dto, BindingResult bindingResult,
      HttpSession session, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("hpid", hpid);
      return "community/post-form";
    }

    try {
      Long userId = (Long) session.getAttribute("loginUser");
      postService.createPost(hpid, dto, userId);
    } catch (CustomException e) {
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());
      model.addAttribute("hpid", hpid);
      return "community/post-form";
    }
    return "redirect:/hospitals/" + hpid + "/posts";

  }

  @GetMapping("/{postId}/edit")
  public String updatePostForm(@PathVariable String hpid,
      @PathVariable Long postId,
      Model model) {
    PostResponseDto post = postService.getPost(postId);
    model.addAttribute("post", post);
    model.addAttribute("postRequestDto", new PostRequestDto());
    model.addAttribute("hpid", hpid);
    return "community/post-form";
  }

  @PostMapping("/{postId}/edit")
  public String updatePost(@PathVariable String hpid, @PathVariable Long postId,
      @Valid @ModelAttribute PostRequestDto dto,
      BindingResult bindingResult, HttpSession session, Model model) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("hpid", hpid);
      return "community/post-form";
    }

    try {
      Long userId = (Long) session.getAttribute("loginUser");
      postService.updatePost(postId, dto, userId);
    } catch (CustomException e) {
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());
      return "community/post-form";
    }
    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }

  @PostMapping("/{postId}/delete")
  public String deletePost(@PathVariable String hpid, @PathVariable Long postId, HttpSession session) {
    Long userId = (Long) session.getAttribute("loginUser");
    String role = (String) session.getAttribute("loginRole");
    postService.deletePost(postId, userId, role);
    return "redirect:/hospitals/" + hpid + "/posts";
  }

}
