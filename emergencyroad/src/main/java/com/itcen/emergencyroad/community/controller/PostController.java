package com.itcen.emergencyroad.community.controller;

import com.itcen.emergencyroad.community.dto.comment.CommentRequestDto;
import com.itcen.emergencyroad.community.dto.comment.CommentResponseDto;
import com.itcen.emergencyroad.community.dto.post.PostRequestDto;
import com.itcen.emergencyroad.community.dto.post.PostResponseDto;
import com.itcen.emergencyroad.community.entity.Comment;
import com.itcen.emergencyroad.community.enums.ReportTargetType;
import com.itcen.emergencyroad.community.service.CommentService;
import com.itcen.emergencyroad.community.service.LikeService;
import com.itcen.emergencyroad.community.service.PostService;
import com.itcen.emergencyroad.community.service.ReportService;
import com.itcen.emergencyroad.global.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hospitals/{hpid}/posts")
public class PostController {

  private final PostService postService;
  private final CommentService commentService;
  private final LikeService likeService;
  private final ReportService reportService;

  @GetMapping
  public String getPosts(@PathVariable String hpid,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) String keyword,
      Model model) {
    Page<PostResponseDto> posts = postService.getPosts(hpid, page, keyword);
    String hospitalName = postService.getHospitalName(hpid);

    model.addAttribute("hospitalName", hospitalName);
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
    Long loginUserId = (Long) session.getAttribute("loginUser");
    List<CommentResponseDto> comments = commentService.getComments(postId, loginUserId);

    long likeCount = likeService.getPostLikeCount(postId);
    boolean isLiked = session.getAttribute("loginUser") != null &&
        likeService.isPostLiked(postId, loginUserId);

    model.addAttribute("post", post);
    model.addAttribute("comments", comments);
    model.addAttribute("commentRequestDto", new CommentRequestDto());
    model.addAttribute("likeCount", likeCount);
    model.addAttribute("isLiked", isLiked);
    model.addAttribute("hpid", hpid);
    return "community/post-detail";
  }

  @GetMapping("/new")
  public String createPostForm(@PathVariable String hpid, Model model){
    model.addAttribute("postRequestDto", new PostRequestDto());
    model.addAttribute("hpid", hpid);
    return "community/post-form";
  }

  @PostMapping
  public String createPost(@PathVariable String hpid,
      @Valid @ModelAttribute PostRequestDto dto, BindingResult bindingResult,
      @RequestParam(required = false) List<MultipartFile> images,
      HttpSession session, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("hpid", hpid);
      return "community/post-form";
    }

    try {
      Long userId = (Long) session.getAttribute("loginUser");
      postService.createPost(hpid, dto, userId, images);
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
      BindingResult bindingResult, @RequestParam(required = false) List<MultipartFile> images,
      HttpSession session, Model model) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("hpid", hpid);
      return "community/post-form";
    }

    try {
      Long userId = (Long) session.getAttribute("loginUser");
      postService.updatePost(postId, dto, userId, images);
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

  @PostMapping("/{postId}/report")
  public String reportPost(@PathVariable String hpid, @PathVariable Long postId,
                           @RequestParam ReportTargetType targetType, HttpSession session){

    Long reporterId = (Long) session.getAttribute("loginUser");

    reportService.createReport(reporterId, targetType, postId);

    return "redirect:/hospitals/" + hpid + "/posts/" + postId;
  }

}
