package com.blog._core.errors;

import com.blog._core.errors.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class MyExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e, HttpServletRequest request) {

        log.warn("=== 400 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류 : {}", e.getMessage());
        log.warn("User-Agent : {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        return "err/400";
    }

    @ExceptionHandler(Exception401.class)
    @ResponseBody
    public ResponseEntity<String> ex401ByData(Exception401 e, HttpServletRequest request) {
        String script = "<script> alert('" + e.getMessage() + "'); location.href = '/login-form' </script>";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.TEXT_HTML)
                .body(script);
    }


    @ExceptionHandler(Exception403.class)
    public String ex403(Exception403 e, HttpServletRequest request) {

        log.warn("=== 403 Forbidden 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류 : {}", e.getMessage());
        log.warn("User-Agent : {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        return "err/403";
    }

    @ExceptionHandler(Exception404.class)
    public String ex404(Exception404 e, HttpServletRequest request) {

        log.warn("=== 404 Not Found 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류 : {}", e.getMessage());
        log.warn("User-Agent : {}", request.getHeader("User-Agent"));

        request.setAttribute("msg", e.getMessage());
        return "err/404";
    }

    @ExceptionHandler(Exception500.class)
    public String ex500(Exception500 e, HttpServletRequest request) {

        log.warn("=== 500 Internal Server Error 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류 : {}", e.getMessage());
        log.warn("User-Agent : {}", request.getHeader("User-Agent"));

        request.setAttribute("msg", e.getMessage());
        return "err/500";
    }


    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, HttpServletRequest request) {

        log.warn("=== 예상 못한 런타임 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류 : {}", e.getMessage());
        log.warn("User-Agent : {}", request.getHeader("User-Agent"));

        request.setAttribute("msg", "시스템 오류 발생. 관리자에게 문의 하세요");
        return "err/500";
    }

}
