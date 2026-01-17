package com.example.demo.controller;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart/add")
    public String addCart(@RequestParam int product_id, @RequestParam int quantity,
                          @RequestParam(required = false) Integer option_id, HttpSession session,
                          RedirectAttributes ra){
        UsersDto loginUser=(UsersDto) session.getAttribute("loginUser");
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인을 해주세요.");
            return "redirect:/login";
        }
        if(quantity<=0){
            ra.addFlashAttribute("msg","수량을 1개 이상 선택하세요.");
            return "redirect:/store/storeItem/"+product_id;
        }
        CartDto cartDto=new CartDto();
        cartDto.setMember_id(loginUser.getMember_id());
        cartDto.setProduct_id(product_id);
        cartDto.setQuantity(quantity);
        cartDto.setOption_id(option_id);
        cartService.insertCart(cartDto);
        ra.addFlashAttribute("msg","장바구니에 담았습니다.");
        return "redirect:/store/storeItem/"+product_id;
    }
}
//account fileinfo cart
//product, option user, musician