package com.example.demo.controller;

import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping("/order/direct")
    public String directOrder(OrderRequestDto requestDto, HttpSession session, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        //로그인확인
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }
        //주문수량확인
        if(requestDto.getQuantity()<=0){
            ra.addFlashAttribute("msg","수량을 1개 이상 선택하세요.");
            return "redirect:/store/storeItem/" + requestDto.getProduct_id();
        }

        try{
            orderService.orderDirect(loginUser.getMember_id(),requestDto);
        }catch (IllegalStateException e){
            ra.addFlashAttribute("msg",e.getMessage());
            return "redirect:/store/storeItem/" + requestDto.getProduct_id();
        }
        ra.addFlashAttribute("msg","주문이 완료되었습니다.");
        return "redirect:/order/complete";
    }
    @PostMapping("/order/direct")
    public String toss_order(){
        return "store/orderPage";
    }
}
