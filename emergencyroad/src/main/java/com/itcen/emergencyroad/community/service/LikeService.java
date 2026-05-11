package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.entity.Comment;
import com.itcen.emergencyroad.community.entity.CommentLike;
import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.PostLike;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.CommentLikeRepository;
import com.itcen.emergencyroad.community.repository.CommentRepository;
import com.itcen.emergencyroad.community.repository.PostLikeRepository;
import com.itcen.emergencyroad.community.repository.PostRepository;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequiredArgsConstructor
public class LikeService {

  private final PostLikeRepository postLikeRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  @Transactional
  public boolean togglePostLike(Long postId, Long userId){
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

    if(postLikeRepository.existsByPost_IdAndUser_Id(postId, userId)){
      postLikeRepository.deleteByPost_IdAndUser_Id(postId, userId);
      return false;
    }

    PostLike postLike = PostLike.create(post, user);
    postLikeRepository.save(postLike);
    return true;
  }

  @Transactional(readOnly = true)
  public long getPostLikeCount(Long postId){
    return postLikeRepository.countByPost_Id(postId);
  }

  @Transactional(readOnly = true)
  public boolean isPostLiked(Long postId, Long userId){
    return postLikeRepository.existsByPost_IdAndUser_Id(postId, userId);
  }

  @Transactional
  public boolean toggleCommentLike(Long commentId, Long userId){
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.COMMENT_NOT_FOUND));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

    if(commentLikeRepository.existsByComment_IdAndUser_Id(commentId, userId)){
      commentLikeRepository.deleteByComment_IdAndUser_Id(commentId, userId);
      return false;
    }

    CommentLike commentLike = CommentLike.create(user, comment);
    commentLikeRepository.save(commentLike);
    return true;
  }

  @Transactional(readOnly = true)
  public long getCommentLikeCount(Long commentId) {
    return commentLikeRepository.countByComment_Id(commentId);
  }

  @Transactional(readOnly = true)
  public boolean isCommentLiked(Long commentId, Long userId) {
    return commentLikeRepository.existsByComment_IdAndUser_Id(commentId, userId);
  }
}
