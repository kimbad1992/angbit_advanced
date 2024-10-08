package com.angbit.angbit_advanced.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {

    @GetMapping("/")
    public String mainPage() {
        return "index"; // 로그인 성공 후 메인 페이지
    }

    @RequestMapping("/login")
    public String login(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/main";
        }
        return "/login";
    }

}
