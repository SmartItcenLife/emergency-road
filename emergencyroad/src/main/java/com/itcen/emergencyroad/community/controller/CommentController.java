package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.comment.CommentRequestDto;
import com.itcen.emergencyroad.community.enums.ReportTargetType;
import com.itcen.emergencyroad.community.service.CommentService;
import com.itcen.emergencyroad.community.service.ReportService;
import com.itcen.emergencyroad.global.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hospitals/{hpid}/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;
  private final ReportService reportService;

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

  // 댓글 신고 접수
  @PostMapping("/{commentId}/report")
  public String reportComment(@PathVariable String hpid,
                              @PathVariable Long postId,
                              @PathVariable Long commentId, // 타겟 번호가 댓글 번호임
                              @RequestParam ReportTargetType targetType,
                              @RequestParam String reason,
                              HttpSession session) {

    Long reporterId = (Long) session.getAttribute("loginUser");

    // 여기서 targetId 자리에 postId가 아니라 'commentId'를 넣어줌
    reportService.createReport(reporterId, targetType, commentId, reason, hpid);

    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }
}
