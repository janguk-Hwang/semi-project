package com.example.demo.controller;

import com.example.demo.dto.OrderConfirmRequestDto;
import com.example.demo.dto.OrderPageDto;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.mapper.CartMapper;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartMapper cartMapper;

    //전체상품 주문
    @PostMapping("/order/cart/all")
    public String orderSelectFromCartAll(HttpSession session,Model model, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }
        List<Integer> cartIds=cartMapper.selectCartIdsByMember(loginUser.getMember_id());

        OrderPageDto orderPage=orderService.makeOrderPageFromCartAll(loginUser.getMember_id(),loginUser);
        orderPage.setOrderSource("CART");
        orderPage.setCartIds(cartIds);

        model.addAttribute("order",orderPage);
        return "store/orderPage";
    }

    //선택상품 주문
    @PostMapping("/order/cart/selected")
    public String orderSelectFromCart(@RequestParam("cartIds") List<Integer> cartIds,
                                      HttpSession session, Model model, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }
        if(cartIds==null || cartIds.isEmpty()){
            ra.addFlashAttribute("msg","주문할 상품을 선택하세요.");
            return "redirect:/mypage/cart";
        }
        OrderPageDto orderPage=orderService.makeOrderPageFromCart(loginUser.getMember_id(),cartIds,loginUser);
        orderPage.setOrderSource("CART");
        orderPage.setCartIds(cartIds);

        model.addAttribute("order",orderPage);
        return "store/orderPage";
    }

    //주문페이지 -> 주문처리(통합)
    @PostMapping("/order/confirm")
    public String orderConfirm(OrderConfirmRequestDto requestDto, HttpSession session, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        if (loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }
        try {
            //주문처리
            orderService.confirmOrder(loginUser.getMember_id(),requestDto);
            ra.addFlashAttribute("msg","주문이 완료되었습니다.");
            return "redirect:/mypage";
        }catch (IllegalStateException e){
            ra.addFlashAttribute("msg",e.getMessage());
            //주문출처 따라 return값 분기
            if ("CART".equals(requestDto.getOrderSource())){
                return "redirect:/mypage/cart";
            }else{
                //DIRECT
                int product_id=requestDto.getItems().get(0).getProduct_id();
                return "redirect:/store/storeItem/"+product_id;
            }

        } catch (Exception e){
            e.printStackTrace();
            ra.addFlashAttribute("msg","주문 처리 중 오류가 발생했습니다.");
            //주문출처 따라 return값 분기
            if("CART".equals(requestDto.getOrderSource())){
                return "redirect:/mypage/cart";
            }else{
                int product_id=requestDto.getItems().get(0).getProduct_id();
                return "redirect:/store/storeItem/"+product_id;
            }
        }
    }



    //상품상세 -> 주문페이지 -> 주문처리
    @PostMapping("/order/direct")
    public String directOrder(OrderRequestDto requestDto, HttpSession session, RedirectAttributes ra){
        UsersDto loginUser=(UsersDto)session.getAttribute("loginUser");
        //로그인확인
        if(loginUser==null){
            ra.addFlashAttribute("msg","로그인이 필요합니다.");
            return "redirect:/login";
        }

        try{
            orderService.orderDirect(loginUser.getMember_id(),requestDto);
            ra.addFlashAttribute("msg","주문이 완료되었습니다.");
            return "redirect:/mypage";
        }catch (IllegalStateException e){
            //재고 부족 등 예외처리
            ra.addFlashAttribute("msg",e.getMessage());
            return "redirect:/store/storeItem/" + requestDto.getProduct_id();
        }catch (Exception e){
            e.printStackTrace();
            ra.addFlashAttribute("msg","주문 처리 중 오류가 발생했습니다.");
            return "redirect:/store/storeItem/" + requestDto.getProduct_id();
        }
    }


}
