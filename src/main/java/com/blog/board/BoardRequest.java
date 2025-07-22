package com.blog.board;

import com.blog.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class BoardRequest {

    // 게시글 저장
    @Data
    public static class SaveDTO {
        @NotEmpty(message = "제목을 입력해주세요")
        @Size(min = 4, max = 20, message = "제목은 4~20자 이내로 작성헤주세요")
        private String title;
        @NotEmpty(message = "내용을 입력해주세요")
        @Size(min = 1, max = 1000, message = "내용은 1~1000자 이내로 작성해주세요")
        private String  content;

        public Board toEntity(User user) {
            return  Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .user(user)
                    .build();
        }
    }

    // 게시글 수정
    @Data
    public static class UpdateDTO {
        @NotEmpty(message = "제목을 입력해주세요")
        @Size(min = 4, max = 20, message = "제목은 4~20자 이내로 작성해주세요")
        private String title;
        @NotEmpty(message = "내용을 입력해주세요")
        @Size(min = 1, max = 1000, message = "내용은 1~1000자 이내로 작성해주세요")
        private String content;
    }


}
