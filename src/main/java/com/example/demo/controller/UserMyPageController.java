package com.example.demo.controller;

import com.example.demo.dto.UsersDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserMyPageController {
    @GetMapping("/mypage")
    public String userMypage(HttpSession httpSession, Model model){
        UsersDto loginUser=(UsersDto)httpSession.getAttribute("loginUser");
        if(httpSession.getAttribute("loginUser")==null){

        }
        if (!"USER".equals(loginUser.getRole())){
            return "redirect:/login";
        }
        return "user/mypageMain";
    }
}
