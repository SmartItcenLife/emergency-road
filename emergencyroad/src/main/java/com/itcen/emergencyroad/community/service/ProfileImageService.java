package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.global.exception.CustomException;
import com.itcen.emergencyroad.global.exception.ExceptionStatus;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {

  private static final String UPLOAD_DIR = "uploads/profiles/";
  private static final List<String> ALLOWED_TYPES = List.of(
      "image/jpeg", "image/jpg", "image/png"
  );

  public String uploadProfileImage(MultipartFile file){
    if(file == null || file.isEmpty()) return null;

    if(!ALLOWED_TYPES.contains(file.getContentType())){
      throw new CustomException(ExceptionStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    try{
      String absoluteUploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIR;
      File uploadDir = new File(absoluteUploadDir);
      if (!uploadDir.exists()) uploadDir.mkdirs();

      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      File dest = new File(absoluteUploadDir + fileName);
      file.transferTo(dest);

      return "/" + UPLOAD_DIR + fileName;
    }catch (IOException e){
      log.error("프로필 이미지 저장 실패: {}", e.getMessage());
      throw new CustomException(ExceptionStatus.FILE_UPLOAD_FAILED);
    }
  }
}

