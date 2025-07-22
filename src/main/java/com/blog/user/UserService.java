package com.blog.user;

import com.blog._core.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true) // 클래스 레벨에서의 읽기 전용 설정
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final ProfileUploadService profileUploadService;

    // 프로필 이미지 업로드
    @Transactional
    public User uploadProfileImage(Long userId, MultipartFile multipartFile) {
        User user = findById(userId);
        String oldImagePath = user.getProfileImagePath();
        try {
            String newImagePath = profileUploadService.uploadProfileImage(multipartFile);
            if (oldImagePath != null) {
                profileUploadService.deleteProfileImage(oldImagePath);
            }
            user.setProfileImagePath(newImagePath);
            return user;

        } catch (IOException e) {
            throw new Exception400("프로필 이미지 업로드에 실패했습니다");
        }
    }

    // 프로필 이미지 삭제
    @Transactional
    public User deleteProfileImage (Long userId) {
        User user = findById(userId);
        String imagePath = user.getProfileImagePath();
        user.setProfileImagePath(null);
        if (imagePath != null && imagePath.isEmpty() == false) {
            profileUploadService.deleteProfileImage(imagePath);
        }
        return user;
    }

    // 회원가입
    @Transactional
    public User join(UserRequest.JoinDTO joinDTO) {
        userJpaRepository.findByUsername(joinDTO.getUsername()).ifPresent(user1 -> {
            throw new Exception400("이미 존재하는 사용자 명 입니다.");
        });
        return userJpaRepository.save(joinDTO.toEntity());
    }

    // 로그인
    public User login(UserRequest.LoginDTO loginDTO) {
        return userJpaRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> {
                    return new Exception400("사용자 명 또는 비밀번호를 확인해주세요.");
                });
    }

    // 사용자 정보 조회
    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception400("사용자를 찾을 수 없습니다.");
        });
    }

    // 회원정보 수정
    @Transactional
    public User updateById(Long userId, UserRequest.UpdateDTO updateDTO) {
        User user = findById(userId);
        user.update(updateDTO);
        return user;
    }


}
