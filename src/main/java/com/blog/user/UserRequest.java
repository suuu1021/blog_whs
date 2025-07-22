package com.blog.user;

import com.blog._core.errors.exception.Exception400;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    // 회원가입
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {
        @NotEmpty(message = "사용자명을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자/2~20자 이내로 작성해주세요")
        private String username;
        @NotEmpty(message = "비밀번호를 입력해주세요")
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 작성해주세요")
        private String password;
        @NotEmpty(message = "이메일은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요")
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
    }

    // 로그인
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        @NotEmpty(message = "사용자명을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자/2~20자 이내로 작성해주세요")
        private String username;
        @NotEmpty(message = "비밀번호를 입력해주세요")
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 작성해주세요")
        private String password;
    }

    // 회원정보 수정
    @Data
    public static class UpdateDTO {
        @NotEmpty(message = "비밀번호를 입력해주세요")
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 작성해주세요")
        private String password;
        @NotEmpty(message = "이메일은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요")
        private String email;
        private String profileImagePath;
    }

    @Data
    public static class ProfileImageDTO {
        private MultipartFile profileImage;

        public void validate() {
            if(profileImage == null || profileImage.isEmpty()) {
                throw new Exception400("프로필 이미지를 선택해주세요");
            }
            if(profileImage.getSize() > 20 * 1024 * 1024) {
                throw new Exception400("파일 크기는 20MB 이하여야 합니다");
            }
            String contentType = profileImage.getContentType();
            if(contentType == null || contentType.startsWith("image/") == false) {
                throw new Exception400("이미지 파일만 업로드 가능합니다");
            }
        }
    }

}
