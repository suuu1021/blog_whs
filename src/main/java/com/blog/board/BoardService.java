package com.blog.board;

import com.blog._core.errors.exception.Exception403;
import com.blog._core.errors.exception.Exception404;
import com.blog.reply.Reply;
import com.blog.user.User;
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

    // 게시글 목록 조회
    public Page<Board> findAll(Pageable pageable) {
        Page<Board> boardPage = boardJpaRepository.findAllJoinUser(pageable);
        return boardPage;
    }

    // 게시글 상세 조회
    public Board findByIdWithReplies(Long id, User sessionUser) {
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(
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

    public Board findById(Long id) {
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(() -> {
            return new Exception404("게시글을 찾을 수 없습니다.");
        });
        return board;
    }

    // 게시글 작성
    @Transactional
    public Board save(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        boardJpaRepository.save(board);
        return board;
    }

    // 게시글 수정
    @Transactional
    public Board updateById(Long id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("해당 게시글이 존재하지 않습니다.");
        });
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정 가능 합니다.");
        }
        board.update(updateDTO);
        return board;
    }

    // 게시글 삭제
    @Transactional
    public void deleteById(Long id, User sessionUser) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("해당 게시글이 존재하지 않습니다.");
        });
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제 가능 합니다");
        }
        boardJpaRepository.deleteById(id);
    }

    // 게시글 소유자 확인
    public void checkBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);
        if (!board.isOwner(userId)) {
            throw new Exception403("본인이 작성한 게시글만 수정 가능 합니다.");
        }
    }

}
