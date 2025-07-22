package com.blog.reply;

import com.blog._core.errors.exception.Exception400;
import com.blog.board.Board;
import com.blog.user.User;
import lombok.Data;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        private Long boardId; // 댓글이 달릴 게시글 id
        private String comment; // 댓글 내용

        public void validate() {
            if (comment == null || comment.trim().isEmpty()) {
                throw new Exception400("댓글 내용을 입력하세요");
            }
            if (comment.length() > 500) {
                throw new Exception400("댓글은 500자 이내로 작성해주세요");
            }
            if (boardId == null) {
                throw new Exception400("게시글 정보가 필요합니다.");
            }
        }


        public Reply toEntity(User sessionUser, Board board) {
            return Reply.builder()
                    .comment(comment.trim())
                    .user(sessionUser)
                    .board(board)
                    .build();
        }
    }

}
