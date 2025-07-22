package com.blog.board;

import com.blog._core.common.PageLink;
import com.blog.user.User;
import com.blog.utils.Define;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    // 메인 화면
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Board> boardPage = boardService.findAll(pageable);

        List<PageLink> pageLinks = new ArrayList<>();
        for (int i = 0; i < boardPage.getTotalPages(); i++) {
            pageLinks.add(new PageLink(i, i + 1, i == boardPage.getNumber()));
        }
        Integer previousPageNumber = boardPage.hasPrevious() ? boardPage.getNumber() : null;
        Integer nextPageNumber = boardPage.hasNext() ? boardPage.getNumber() + 2 : null;

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("pageLinks", pageLinks);
        model.addAttribute("previousPageNumber", previousPageNumber);
        model.addAttribute("nextPageNumber", nextPageNumber);
        return "index";
    }

    // 게시글 상세보기
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        Board board = boardService.findByIdWithReplies(id,sessionUser);
        model.addAttribute("board", board);
        return "board/detail";
    }

    // 게시글 작성 화면 요청
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    // 게시글 작성
    @PostMapping("/board/save")
    public String save(@Valid BoardRequest.SaveDTO reqDTO, Errors errors, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.save(reqDTO, sessionUser);
        return "redirect:/";
    }

    // 게시글 수정 화면 요청
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long boardId,
                             HttpServletRequest request, HttpSession session) {
        // 인증, 권한
        User sessionUSer = (User) session.getAttribute(Define.SESSION_USER);
        boardService.checkBoardOwner(boardId, sessionUSer.getId());
        request.setAttribute("board", boardService.findById(boardId));
        return "board/update-form";
    }

    // 게시글 수정
    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,
                         @Valid BoardRequest.UpdateDTO reqDTO, Errors errors,
                         HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.updateById(id, reqDTO, sessionUser);
        return "redirect:/board/" + id;
    }


    // 게시글 삭제
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.deleteById(id, sessionUser);
        return "redirect:/";
    }
}
