package com.blog.reply;

import com.blog.board.Board;
import com.blog.user.User;
import com.blog.utils.MyDateUtil;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

    @Data
    @NoArgsConstructor
    @Table(name = "reply_tb")
    @Entity
    public class Reply {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 500)
        private String comment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "board_id")
        private Board board;

        @CreationTimestamp
        private Timestamp createdAt;

        @Builder
        public Reply(Long id, String comment, User user, Board board, Timestamp createdAt) {
            this.id = id;
            this.comment = comment;
            this.user = user;
            this.board = board;
            this.createdAt = createdAt;
        }

        @Transient
        private boolean isReplyOwner;

        public boolean isOwner(Long sessionId) {
            return this.user.getId().equals(sessionId);
        }

        public String getTime() {
            return MyDateUtil.timestampFormat(createdAt);
        }

    }