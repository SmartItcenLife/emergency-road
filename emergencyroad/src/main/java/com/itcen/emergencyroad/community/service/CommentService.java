package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.comment.CommentRequestDto;
import com.itcen.emergencyroad.community.dto.comment.CommentResponseDto;
import com.itcen.emergencyroad.community.entity.Comment;
import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.enums.Role;
import com.itcen.emergencyroad.community.repository.CommentLikeRepository;
import com.itcen.emergencyroad.community.repository.CommentRepository;
import com.itcen.emergencyroad.community.repository.PostRepository;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentLikeRepository commentLikeRepository;

  @Transactional(readOnly = true)
  public List<CommentResponseDto> getComments(Long postId, Long loginUserId) {

    List<Comment> comments = commentRepository
        .findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId);

    List<Long> commentIds = comments.stream().map(Comment::getId).toList();

    Map<Long, Long> likeCountMap = commentLikeRepository.countByCommentIdIn(commentIds)
        .stream()
        .collect(Collectors.toMap(
            row -> (Long) row[0],
            row -> (Long) row[1]
        ));

    Set<Long> likedCommentIds = loginUserId != null
        ? new HashSet<>(commentLikeRepository.findLikedCommentIdsByUserIdIn(commentIds, loginUserId))
        : Collections.emptySet();

    return comments.stream()
        .map(comment -> CommentResponseDto.from(
            comment,
            likeCountMap.getOrDefault(comment.getId(), 0L),
            likedCommentIds.contains(comment.getId())
        )).toList();
  }

  @Transactional
  public void createComment(Long postId, CommentRequestDto dto, Long userId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

    Comment comment = Comment.builder()
        .post(post)
        .user(user)
        .content(dto.getContent())
        .build();
    commentRepository.save(comment);
  }

  @Transactional
  public void updateComment(Long commentId, CommentRequestDto dto, Long userId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(ExceptionStatus.USER_COMMENT_FORBIDDEN);
    }

    comment.update(dto.getContent());
  }

  @Transactional
  public void deleteComment(Long commentId, Long userId, String role) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.NOT_FOUND));

    boolean isAuthor = comment.getUser().getId().equals(userId);
    boolean isAdmin = Role.ADMIN.name().equals(role);

    if (!isAuthor && !isAdmin) {
      throw new CustomException(ExceptionStatus.DELETE_FORBIDDEN);
    }

    comment.delete();
  }
}
