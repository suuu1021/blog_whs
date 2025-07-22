package com.blog.board;

import com.blog.reply.Reply;
import com.blog.user.User;
import com.blog.utils.MyDateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "board_tb")
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    @Transient
    private boolean isBoardOwner;

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

    public String getTime() {
        return MyDateUtil.timestampFormat(createdAt);
    }

    public void update(BoardRequest.UpdateDTO updateDTO) {
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();
    }

    @OrderBy("id DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.REMOVE)
    List<Reply> replies = new ArrayList<>();

}
