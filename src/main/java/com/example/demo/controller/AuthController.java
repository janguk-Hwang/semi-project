package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.AccountService;
import com.example.demo.service.SignupService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UsersService userservice;
    private final SignupService signupService;

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
    public String signup(@Valid SignupRequestDto dto, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {

        // 1) @Valid 검증 실패하면 회원가입 페이지로 다시
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupRequestDto",dto);
            return "auth/signup";
        }

        // 2) 비밀번호 확인 체크 (null 안전)
        if (dto.getPwd() == null || !dto.getPwd().equals(dto.getPwdCheck())) {
            bindingResult.rejectValue("pwdCheck", "password.mismatch", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("signupRequestDto",dto);
            return "auth/signup";
        }

        try {
            // 3) 서비스에서 users + account 트랜잭션으로 저장
            signupService.signup(dto);

            // 4) 성공 메시지 (리다이렉트로 전달하려면 flash 사용)
            redirectAttributes.addFlashAttribute("msg", "회원가입 완료");
            return "redirect:/login";

        } catch (Exception e) {
            // 중복 아이디 등 예외 처리
            bindingResult.reject("signup.failed", "회원가입 실패");
            model.addAttribute("signupRequestDto",dto);
            return "auth/signup";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "home";
    }
}


