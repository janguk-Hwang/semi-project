package com.example.demo.controller;

import com.example.demo.dto.CartListDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserCartController {
    private final CartService cartService;
    @GetMapping("/mypage/cart")
    public String mypageCart(HttpSession httpSession, Model model){
        UsersDto loginUser=(UsersDto)httpSession.getAttribute("loginUser");
        if(loginUser==null){
            return "redirect:/login";
        }
        int member_id=loginUser.getMember_id();

        List<CartListDto> cartList=cartService.selectCartList(member_id);

        model.addAttribute("cartlist",cartList);
        model.addAttribute("cartCount",cartList.size());

        return "user/mypageCart";
    }
}
