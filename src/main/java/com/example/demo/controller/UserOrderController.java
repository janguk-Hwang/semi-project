package com.example.demo.controller;

import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserOrderController {
    private final OrderService orderService;

    //주문내역 -> 주문별 상세내역 페이지 이동
    @GetMapping("/mypage/orders/{orderId}")
    public String orderDetail(@PathVariable int orderId, HttpSession session, Model model, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }
        try{
            OrderDetailDto order=orderService.getOrderDetail(orderId,loginUser.getMember_id());
            model.addAttribute("order",order);
            return "user/orderListDetail";
        }catch (IllegalArgumentException e){
            ra.addFlashAttribute("msg",e.getMessage());
            return "redirect:/mapage/orders";
        }
    }
}
