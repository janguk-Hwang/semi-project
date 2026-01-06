package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage(){
        return "auth/loginPage";
    }
    @GetMapping("/signup")
    public String joinPage(){
        return "auth/signup";
    }
//    @PostMapping("/signup")
//    public String signup(){
//
//    }
}
