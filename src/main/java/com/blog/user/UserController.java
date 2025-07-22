package com.blog.user;

import com.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    // 회원가입 화면
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(@Valid UserRequest.JoinDTO joinDTO, Errors errors) {
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    // 로그인 화면 요청
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpSession session) {
        User user = userService.login(loginDTO);
        session.setAttribute(Define.SESSION_USER, user);
        return "redirect:/";
    }

    // 회원정보 수정 화면
    @GetMapping("/user/update-form")
    public String updateForm(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    // 회원정보 수정
    @PostMapping("/user/update")
    public String update(@Valid UserRequest.UpdateDTO reqDTO, Errors errors,
                         HttpSession session) {
        User user = (User) session.getAttribute("sessionUser");
        User updateUser = userService.updateById(user.getId(), reqDTO);
        session.setAttribute("sessionUser", updateUser);
        return "redirect:/user/update-form";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
