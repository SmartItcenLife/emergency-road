package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.post.PostRequestDto;
import com.itcen.emergencyroad.community.dto.post.PostResponseDto;
import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.User;
import com.itcen.emergencyroad.community.repository.PostRepository;
import com.itcen.emergencyroad.community.repository.UserRepository;
import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  static final String ADMIN_ROLE = "ADMIN";

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final HospitalRepository hospitalRepository;

  @Transactional
  public void createPost(String hpid, PostRequestDto dto, Long userId){
    User user = userRepository.findById(userId).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_FOUND));

    Hospital hospital = hospitalRepository.findByHpid(hpid).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_FOUND));

    Post post = Post.create(user, hospital, dto.getTitle(), dto.getContent());
    postRepository.save(post);
  }

  @Transactional
  public void deletePost(Long postId, Long userId, String role){
    Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    boolean isAuthor = post.getUser().getId().equals(userId);
    boolean isAdmin = ADMIN_ROLE.equals(role);

    if(!isAdmin && !isAuthor) throw new CustomException(ExceptionStatus.FORBIDDEN);
  }

  @Transactional
  public void updatePost(Long postId, PostRequestDto dto, Long userId){

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    if(!post.getUser().getId().equals(userId)) throw new CustomException(ExceptionStatus.USER_POST_FORBIDDEN);

    post.update(dto.getTitle(), dto.getContent());
  }

  @Transactional(readOnly = true)
  public PostResponseDto getPost(Long postId){
    Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ExceptionStatus.POST_NOT_FOUND));

    if(post.isDeleted()) throw new CustomException(ExceptionStatus.POST_NOT_FOUND);

    return PostResponseDto.from(post);
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

    return posts.map(PostResponseDto::from);
  }
}
