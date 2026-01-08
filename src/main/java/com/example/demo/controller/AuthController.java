package com.example.demo.controller;

import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.User_rolesDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.SignupService;
import com.example.demo.service.UsermanagementService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final SignupService signupService;
    private final UsersService usersService;
    private final UsermanagementService usermanagementService;

    @GetMapping("/login")
    public String loginPage(){
        return "auth/loginPage";
    }

    @PostMapping("/login")
    public String login(UsersDto usersDto, HttpSession httpSession,Model model){
        UsersDto dto=usersService.isUser(usersDto);
        User_rolesDto adminDto=usermanagementService.isadmin(dto.getMember_id());
        if(dto==null){
            model.addAttribute("errorMsg","아이디 또는 비밀번호가 맞지 않습니다.");
            return "auth/loginPage";
        }else{
            httpSession.setAttribute("loginUser", dto);
            return "redirect:/";
        }
    }

    @GetMapping("/signup")
    public String joinPage(Model model){
        model.addAttribute("signupRequestDto",new SignupRequestDto());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupRequestDto dto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes ,Model model){
        if(!dto.getPwd().equals(dto.getPwdCheck())){
            model.addAttribute("signupRequestDto", dto);
            return "auth/signup";
        }

        if(bindingResult.hasErrors()){
            return "auth/signup";
        }
        signupService.signup(dto);
        redirectAttributes.addFlashAttribute("msg","회원가입이 완료되었습니다.");
        return "redirect:/login";
    }

    @GetMapping("/existId")
    @ResponseBody
    public Map<String,Object> existId(@RequestParam("id") String id){
        UsersDto usersDto=usersService.selectOne(id);
        boolean exist=usersDto!=null;
        Map<String,Object> map=new HashMap<>();
        map.put("result",exist);
        return map;
    }
    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/";
    }
}
