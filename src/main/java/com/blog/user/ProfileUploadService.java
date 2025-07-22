package com.blog.user;

import com.blog._core.errors.exception.Exception400;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ProfileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 프로필 이미지 파일을 서버에 업로드
    public String uploadProfileImage(MultipartFile multipartFile) throws IOException {
        createUploadDirectory();
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String uniqueFileName = generateUniqueFileName(extension);
        Path filePath = Paths.get(uploadDir, uniqueFileName);
        multipartFile.transferTo(filePath);
        return "/uploads/profiles/" + uniqueFileName;
    }

    // 고유한 파일명 생성
    private String generateUniqueFileName(String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uuid + extension;
    }

    // 파일 확장자만 추출
    private String getFileExtension(String originalFileName) {
        if (originalFileName == null || originalFileName.lastIndexOf(".") == -1) {
            return "";
        }
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    // 폴더를 생성
    private void createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (Files.exists(uploadPath) == false) {
            Files.createDirectories(uploadPath);
        }
    }

    // 프로필 이미지 삭제
    public void deleteProfileImage(String imagePath) {
        if (imagePath != null && imagePath.isEmpty() == false) {
            try {
                String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                Path filePath = Paths.get(uploadDir, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new Exception400("프로필 이미지를 삭제하지 못하였습니다");
            }
        }

    }
}