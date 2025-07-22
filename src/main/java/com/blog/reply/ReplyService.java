package com.blog.reply;

import com.blog._core.errors.exception.Exception403;
import com.blog._core.errors.exception.Exception404;
import com.blog.board.Board;
import com.blog.board.BoardJpaRepository;
import com.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyJpaRepository replyJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    @Transactional
    public void save(ReplyRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = boardJpaRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new Exception404("존재하지 않는 게시글에는 댓글 작성이 불가합니다."));
        Reply reply = saveDTO.toEntity(sessionUser, board);
        replyJpaRepository.save(reply);
    }

    // 댓글 삭제 기능
    @Transactional
    public void deleteById(Long replyId, User sessionUser) {
        Reply reply = replyJpaRepository.findById(replyId).orElseThrow(() -> new Exception404("삭제할 댓글이 없습니다."));
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        replyJpaRepository.deleteById(replyId);
    }
}
