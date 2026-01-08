package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.AccountService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/login")
    public String login(UsersDto usersdto, HttpSession session, Model model) {

        UsersDto dto = userservice.isUser(usersdto);
        if (dto == null) {
            model.addAttribute("error", "아이디또는 비밀번호가 틀립니다.");
            return "auth/loginPage";
        } else {
            session.setAttribute("loginUser", dto);
            return "redirect:/";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String pwdCheck,
                         @ModelAttribute("account") AccountDto accountDto,
                         @ModelAttribute("users") UsersDto usersDto, Model model) {
        String pwd=usersDto.getPwd();
        if (!pwd.equals(pwdCheck)|| pwd == null) {
            model.addAttribute("result", "비밀번호를 다시 확인해 주십시오");
            return "redirect:/auth/signup";
        }

        int user = userservice.insertUser(usersDto);
        if (user == 0){
            model.addAttribute("result", "회원가입 실패");
            return "redirect:/auth/signup";
        }

        int mnum= usersDto.getMember_id();
        accountDto.setMember_id(mnum);

        int account = accountService.insertAccount(accountDto);
        if (account == 0) {
            model.addAttribute("result", "회원가입 실패");
        }

        model.addAttribute("result", "회원가입 완료");
        return "auth/loginPage";
    }
}


