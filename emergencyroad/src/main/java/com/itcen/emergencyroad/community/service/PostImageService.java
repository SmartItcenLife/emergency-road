package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.entity.Post;
import com.itcen.emergencyroad.community.entity.PostImage;
import com.itcen.emergencyroad.community.repository.PostImageRepository;
import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostImageService {

  private final PostImageRepository postImageRepository;

  private static final String UPLOAD_DIR = "uploads/";
  private static final int MAX_IMAGE_COUNT = 3;
  private static final List<String> ALLOWED_TYPES = List.of(
      "image/jpeg", "image/jpg", "image/png", "image/gif"
  );

  @Transactional
  public void uploadImages(Post post, List<MultipartFile> images){
    if(images == null || images.isEmpty()) return;

    List<MultipartFile> validImages = images.stream()
        .filter(image -> !image.isEmpty())
        .toList();

    if(validImages.isEmpty()) return;

    if(validImages.size() > MAX_IMAGE_COUNT) {
      throw new CustomException(ExceptionStatus.IMAGE_LIMIT_EXCEEDED);
    }

    for(MultipartFile image : validImages){
      log.info("contentType: {}", image.getContentType());
      if(!ALLOWED_TYPES.contains(image.getContentType())){
        throw new CustomException(ExceptionStatus.UNSUPPORTED_MEDIA_TYPE);
      }

      String imageUrl = saveFile(image);
      PostImage postImage = PostImage.create(post, imageUrl);
      postImageRepository.save(postImage);
    }

  }

  @Transactional
  public void deleteImages(Long postId){
    postImageRepository.deleteByPost_Id(postId);
  }

  @Transactional(readOnly = true)
  public List<String> getImageUrls(Long postId){
    return postImageRepository.findByPost_IdOrderByCreatedAtAsc(postId)
        .stream()
        .map(PostImage::getImageUrl)
        .toList();
  }

  private String saveFile(MultipartFile file){
    try{
      String absoluteUploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIR;
      File uploadDir = new File(absoluteUploadDir);
      log.info("uploadDir 절대경로: {}", uploadDir.getAbsolutePath());  // 추가
      log.info("exists: {}", uploadDir.exists());                        // 추가
      log.info("mkdirs 결과: {}", uploadDir.mkdirs());

      if(!uploadDir.exists()) uploadDir.mkdirs();

      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      File dest = new File(absoluteUploadDir + fileName);
      file.transferTo(dest);

      return "/" + UPLOAD_DIR + fileName;
    } catch (IOException e){
      log.error("파일 저장 실패: {}", e.getMessage());
      throw new CustomException(ExceptionStatus.FILE_UPLOAD_FAILED);
    }
  }
}
