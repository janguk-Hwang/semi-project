package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminProductController {
    @GetMapping("/admin/product")
    public String adminproduct(){
        return "admin/product";
    }
}
