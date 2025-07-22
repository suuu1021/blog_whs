package com.blog.board;

import com.blog._core.errors.exception.Exception403;
import com.blog._core.errors.exception.Exception404;
import com.blog.reply.Reply;
import com.blog.user.SessionUser;
import com.blog.user.User;
import com.blog.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardJpaRepository boardJpaRepository;
    private final UserJpaRepository userJpaRepository;

    // 게시글 목록 조회
    public Page<Board> findAll(Pageable pageable) {
        Page<Board> boardPage = boardJpaRepository.findAllJoinUser(pageable);
        return boardPage;
    }

    // 게시글 상세 조회
    public Board findByIdWithReplies(Long id, SessionUser sessionUser) {
        Board board = boardJpaRepository.findByIdWithDetails(id).orElseThrow(
                () -> new Exception404("게시글을 찾을 수 없습니다."));
        if (sessionUser != null) {
            boolean isBoardOwner = board.isOwner(sessionUser.getId());
            board.setBoardOwner(isBoardOwner);
        }
        List<Reply> replies = board.getReplies();
        if (sessionUser != null) {
            replies.forEach(reply -> {
                boolean isReplyOwner = reply.isOwner(sessionUser.getId());
                reply.setReplyOwner(isReplyOwner);
            });
        }
        return board;
    }

    // 게시글 작성
    @Transactional
    public Board save(BoardRequest.SaveDTO saveDTO, SessionUser sessionUser) {
        User user = userJpaRepository.getReferenceById(sessionUser.getId());
        Board board = saveDTO.toEntity(user);
        boardJpaRepository.save(board);
        return board;
    }

    // 게시글 수정
    @Transactional
    public Board updateById(Long id, BoardRequest.UpdateDTO updateDTO, SessionUser sessionUser) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("게시글을 찾을 수 없습니다.");
        });
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정 가능 합니다.");
        }
        board.update(updateDTO);
        return board;
    }

    // 게시글 삭제
    @Transactional
    public void deleteById(Long id, SessionUser sessionUser) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("해당 게시글이 존재하지 않습니다.");
        });
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제 가능 합니다");
        }
        boardJpaRepository.deleteById(id);
    }

}
