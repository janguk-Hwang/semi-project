package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminBoardController {
    @GetMapping("/admin/board")
    public String adminboard(){
        return "admin/board";
    }
}
