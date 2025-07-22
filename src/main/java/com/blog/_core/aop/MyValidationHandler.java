package com.blog._core.aop;

import com.blog._core.errors.exception.Exception400;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Aspect
@Component
@Slf4j
public class MyValidationHandler {

    @Before("@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void validationCheck(JoinPoint joinPoint) {
        log.debug("=== AOP 유효성 검사 시작 ===");
        log.debug("실행 메서드 - {}", joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                log.debug("Errors 객체 발견 - 유효성 검사 진행");
                Errors errors = (Errors) arg;
                if (errors.hasErrors()) {
                    log.warn("유효성 검사 오류 발견 - 오류 개수 : {}", errors.getErrorCount());
                    FieldError firstError = errors.getFieldErrors().get(0);
                    String errorMessage = firstError.getDefaultMessage() + " : " + firstError.getField();
                    throw new Exception400(errorMessage);
                }
                log.debug("유효성 검사 통과");
                break;
            }
        }
        log.debug("=== AOP 유효성 검사 완료 ===");
    }
}
