package com.example.demo.controller;

import com.example.demo.dto.CartListDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/cart/updateCartQuantity")
    @ResponseBody
    public String updateUserCartQuantity(@RequestParam int cart_id,@RequestParam int quantity, HttpSession httpSession){
        UsersDto loginUser=(UsersDto) httpSession.getAttribute("loginUser");
        if(loginUser==null){
            return "login_required";
        }
        if(quantity<=0){
            return "invalid_quantity";
        }
        cartService.updateUserCartQuantity(cart_id,quantity);
        return "ok";
    }

    @PostMapping("/cart/deleteSelected")
    @ResponseBody
    public String deleteSelectedCart(@RequestParam("cart_id") List<Integer> cart_id,HttpSession httpSession){
        UsersDto loginUser=(UsersDto)httpSession.getAttribute("loginUser");
        if(loginUser==null){
            return "login_required";
        }
        if(cart_id==null || cart_id.isEmpty()){
            return "no_item";
        }
        int member_id=loginUser.getMember_id();
        for(Integer cartId : cart_id){
            cartService.deleteCartItem(cartId,member_id);
        }
        return "ok";
    }
}
