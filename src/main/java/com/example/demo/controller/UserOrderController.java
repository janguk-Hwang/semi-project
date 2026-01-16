package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.AccountService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserOrderController {
    private final OrderService orderService;
    private final AccountService accountService;

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
            return "redirect:/mypage/orders";
        }
    }

    //주문내역 -> 주문취소 페이지 이동
    @GetMapping("/myorder/cancel/{orderId}")
    public String orderCancelPageForm(@PathVariable("orderId") int orderId,
                                      HttpSession session, Model model, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto) session.getAttribute("loginUser");
        if(loginUser==null){
            return "redirect:/login";
        }
        int member_id=loginUser.getMember_id();
        //주문취소 가능여부 체크(본인주문 + ORDERED상태)
        if(!orderService.isCancelableOrder(orderId,member_id)){
            ra.addFlashAttribute("msg","취소할 수 없는 주문입니다.");
            return "redirect:/mypage/orders"+orderId;
        }
        //취소대상 주문 정보 조회(상품목록 포함)
        OrderDetailDto order=orderService.getOrderDetail(orderId,member_id);
        //환불계좌 정보 조회
        AccountDto account=accountService.selectAccountByMemberId(member_id);

        model.addAttribute("order",order);
        model.addAttribute("account",account);
        return "user/orderCancel";
    }

    //최종 주문취소
    @PostMapping("/myorder/cancel")
    public String cancelOrder(@RequestParam("order_id") int orderId, AccountDto accountDto,
                              HttpSession session, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "/login";
        }
        int member_id=loginUser.getMember_id();
        try{
            //환불계좌정보 저장
            accountDto.setMember_id(member_id);
            AccountDto saveAccount=accountService.selectAccountByMemberId(member_id);
            if(saveAccount==null){
                accountService.insertAccount(accountDto);
            }else{
                accountService.updateAccount(accountDto);
            }
            //주문 취소 처리(재고복구+상태변경)
            orderService.cancelOrder(orderId,member_id);
            ra.addFlashAttribute("msg","주문이 정상적으로 취소되었습니다.");
            return "redirect:/mypage/orders/"+orderId;
        }catch (IllegalStateException e){
            //비즈니스 예외(취소불가,재고복구실패)
            ra.addFlashAttribute("msg",e.getMessage());
            return "redirect:/mypage/orders/"+orderId;
        }catch (Exception e){
            ra.addFlashAttribute("msg","주문 취소 중 오류가 발생했습니다.");
            return "redirect:/mypage/orders/"+orderId;
        }
    }
}
