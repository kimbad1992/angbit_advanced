package com.angbit.angbit_advanced.common.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Exception caught", e);
        return ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())
                .build();
    }

    // 특정 Exception을 Handling할 시
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException", ex);
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Invalid request parameter")
                .property("errorDetails", ex.getMessage())
                .build();
    }

    /* 페이지 URL을 Model에 담기 위해 사용 */
    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    /* 세션 유지 시간 표기를 위해 사용 */
    @ModelAttribute("sessionTime")
    Long getSessionRemainingTime(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            long currentTime = System.currentTimeMillis();
            // 세션 만료 시간 - 현재 시간 = 잔여 시간
            long remainingTime = session.getMaxInactiveInterval() * 1000L - (currentTime - session.getLastAccessedTime());
            return Math.max(remainingTime, 0); // 음수가 되지 않도록 조정
        }
        return null;
    }
}
