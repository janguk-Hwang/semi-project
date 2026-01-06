package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.AccountService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UsersService userservice;
    private final AccountService accountService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/loginPage";
    }

    @GetMapping("/signup")
    public String joinPage() {
        return "auth/signup";
    }
//    @PostMapping("/signup")
//    public String signup(){
//
//    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        String id = request.getParameter("id");
        String pwd = request.getParameter("pwd");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("pwd", pwd);
        UsersDto dto =userservice.isMembers(map);
        if (dto == null) {
            model.addAttribute("error", "아이디또는 비밀번호가 틀립니다.");
            return "auth/loginPage";
        } else {
            session.setAttribute("login", dto);
            return "redirect:/";
        }
    }
    @PostMapping("/signup")
    public String signup(AccountDto accountDto,UsersDto usersDto,Model model){
        int user=userservice.insertUser(usersDto);
        int account=accountService.insertAccount(accountDto);
        if (user==0 || account==0){
            model.addAttribute("result","회원가입 실패");
        }else{
            model.addAttribute("result","회원가입 완료");
        }
        return "auth/loginPage";
    }
}


