package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.comment.CommentRequestDto;
import com.itcen.emergencyroad.community.service.CommentService;
import com.itcen.emergencyroad.global.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hospitals/{hpid}/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public String createComment(@PathVariable String hpid, @PathVariable Long postId,
      @Valid @ModelAttribute CommentRequestDto dto,
      BindingResult bindingResult,
      HttpSession session, Model model){
    if(bindingResult.hasErrors()) return "redirect:/hospitals/" + hpid + "/posts" + postId;

    try{
      Long userId  = (Long) session.getAttribute("loginUser");
      commentService.createComment(postId, dto, userId);
    } catch (CustomException e) {
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());
    }
    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }

  @PostMapping("/{commentId}/edit")
  public String updateComment(@PathVariable String hpid,
      @PathVariable Long postId,
      @PathVariable Long commentId,
      @Valid @ModelAttribute CommentRequestDto dto,
      BindingResult bindingResult,
      HttpSession session,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "redirect:/hospitals/" + hpid + "/posts/" + postId;
    }

    try {
      Long userId = (Long) session.getAttribute("loginUser");
      commentService.updateComment(commentId, dto, userId);
    } catch (CustomException e) {
      model.addAttribute("errorMessage", e.getExceptionStatus().getMessage());
    }

    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }

  // 댓글 삭제
  @PostMapping("/{commentId}/delete")
  public String deleteComment(@PathVariable String hpid,
      @PathVariable Long postId,
      @PathVariable Long commentId,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("loginUser");
    String role = (String) session.getAttribute("loginRole");
    commentService.deleteComment(commentId, userId, role);
    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }
}
