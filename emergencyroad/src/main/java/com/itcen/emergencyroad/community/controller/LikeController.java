package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.service.LikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hospitals/{hpid}/posts/{postId}")
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/like")
  public String togglePostlike(@PathVariable String hpid, @PathVariable Long postId, HttpSession session){
    Long userId = (Long) session.getAttribute("loginUser");
    likeService.togglePostLike(postId, userId);
    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }

  @PostMapping("/comments/{commentId}/like")
  public String toggleCommentLike(@PathVariable String hpid, @PathVariable Long postId, @PathVariable Long commentId,
      HttpSession session){
    Long userId = (Long) session.getAttribute("loginUser");
    likeService.toggleCommentLike(commentId, userId);
    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }
}
