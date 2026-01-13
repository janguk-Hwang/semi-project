package com.example.demo.controller;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.OrderPageDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/cart/add")
    public String addCart(@RequestParam int product_id, @RequestParam int quantity,
                          @RequestParam(required = false) Integer option_id, HttpSession session,
                          @RequestParam String action, RedirectAttributes ra, Model model) {
        UsersDto loginUser = (UsersDto) session.getAttribute("loginUser");
        //로그인 체크
        if (loginUser == null) {
            ra.addFlashAttribute("msg", "로그인을 해주세요.");
            return "redirect:/login";
        }
        //주문수량 체크
        if (quantity <= 0) {
            ra.addFlashAttribute("msg", "수량을 1개 이상 선택하세요.");
            return "redirect:/store/storeItem/" + product_id;
        }
        //장바구니 버튼인 경우
        if("cart".equals(action)){
            CartDto cartDto = new CartDto();
            cartDto.setMember_id(loginUser.getMember_id());
            cartDto.setProduct_id(product_id);
            cartDto.setQuantity(quantity);
            cartDto.setOption_id(option_id);

            cartService.addCart(cartDto);
            ra.addFlashAttribute("msg", "장바구니에 담았습니다.");
            return "redirect:/store/storeItem/" + product_id;
        }
        //주문하기 버튼인 경우
        if("order".equals(action)){
            OrderPageDto orderPage=orderService.makeOrderPage(product_id,option_id,quantity,loginUser);
            model.addAttribute("order",orderPage);

            return "store/orderPage";
        }
        return "redirect:/store/storeItem/" + product_id;
    }
}
//account fileinfo cart
//product, option user, musician