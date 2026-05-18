package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.post.PostRequestDto;
import com.itcen.emergencyroad.community.dto.post.PostResponseDto;
import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.CommentRepository;
import com.itcen.emergencyroad.community.repository.PostImageRepository;
import com.itcen.emergencyroad.community.repository.PostLikeRepository;
import com.itcen.emergencyroad.community.repository.PostRepository;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  static final String ADMIN_ROLE = "ADMIN";

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final HospitalRepository hospitalRepository;
  private final PostImageService postImageService;
  private final LikeService likeService;
  private final CommentService commentService;
  private final PostLikeRepository postLikeRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public void createPost(String hpid, PostRequestDto dto, Long userId,
      List<MultipartFile> images){
    User user = userRepository.findById(userId).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_FOUND));

    Hospital hospital = hospitalRepository.findByHpid(hpid).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_FOUND));

    Post post = Post.create(user, hospital, dto.getTitle(), dto.getContent());
    postRepository.save(post);

    postImageService.uploadImages(post, images);
  }

  @Transactional
  public void deletePost(Long postId, Long userId, String role){
    Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    boolean isAuthor = post.getUser().getId().equals(userId);
    boolean isAdmin = ADMIN_ROLE.equals(role);

    if(!isAdmin && !isAuthor) throw new CustomException(ExceptionStatus.DELETE_FORBIDDEN);

    postImageService.deleteImages(postId);
    post.delete();
  }

  @Transactional
  public void updatePost(Long postId, PostRequestDto dto, Long userId,
      List<MultipartFile> images){

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    if(!post.getUser().getId().equals(userId)) throw new CustomException(ExceptionStatus.USER_POST_FORBIDDEN);

    post.update(dto.getTitle(), dto.getContent());

    postImageService.deleteImages(postId);
    postImageService.uploadImages(post, images);
  }

  @Transactional(readOnly = true)
  public PostResponseDto getPost(Long postId){
    Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

//    if(post.isDeleted()) throw new CustomException(ExceptionStatus.POST_NOT_FOUND);

    List<String> imageUrls = postImageService.getImageUrls(postId);

    long postLikeCount = postLikeRepository.countByPost_Id(postId);
    long commentLikeCount = commentRepository.countByPostIdAndIsDeletedFalse(postId);

    return PostResponseDto.from(post, imageUrls, postLikeCount, commentLikeCount);
  }

  @Transactional(readOnly = true)
  public Page<PostResponseDto> getPosts(String hpid, int page, String keyword){
    Pageable pageable = PageRequest.of(page, 10, Sort.by(Direction.DESC,"createdAt"));

    Page<Post> posts;

    if(keyword != null && !keyword.isBlank()){
      posts = postRepository.searchByHospitalId(hpid, keyword, pageable);
    } else {
      posts = postRepository.findByHospitalHpidAndIsDeletedFalse(hpid, pageable);
    }

    return posts.map(post -> {
      List<String> imageUrls = postImageService.getImageUrls(post.getId());
      long postLikeCount = postLikeRepository.countByPost_Id(post.getId());
      long commentCount = commentRepository.countByPostIdAndIsDeletedFalse(post.getId());
      return PostResponseDto.from(post, imageUrls, postLikeCount, commentCount);
    });
  }

  @Transactional(readOnly = true)
  public String getHospitalName(String hpid){
     return hospitalRepository.findHospitalByHpid(hpid);
  }
}
