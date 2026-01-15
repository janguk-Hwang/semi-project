package com.example.demo.controller;

import com.example.demo.dto.OrderListDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserMyPageController {
    private final OrderService orderService;

    //마이페이지 Main
    @GetMapping("/mypage")
    public String userMypage(HttpSession httpSession, Model model, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)httpSession.getAttribute("loginUser");
        if(httpSession.getAttribute("loginUser")==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }
        if (!"USER".equals(loginUser.getRole())){
            return "redirect:/login";
        }
        return "user/mypageMain";
    }

    //마이페이지 -> 주문/배송내역 페이지
    @GetMapping("/mypage/orders")
    public String userOrderListPage(@RequestParam(defaultValue = "1") int pageNum, HttpSession session,
                                    Model model, RedirectAttributes ra){
         UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
         if(loginUser==null){
             ra.addFlashAttribute("msg","로그인이 필요합니다.");
             return "redirect:/login";
         }
         Map<String,Object> result=orderService.getOrderListPaging(loginUser.getMember_id(),pageNum);
         model.addAttribute("orderList",result.get("orderList"));
         model.addAttribute("pageInfo",result.get("pageInfo"));
         model.addAttribute("totalCount",result.get("totalCount"));

         return "user/mypageOrderList";
    }
}
