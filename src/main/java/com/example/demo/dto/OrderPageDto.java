package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderPageDto { //주문 "페이지 렌더링용" DTO
    //주문상품 목록
    private List<OrderPageItemDto> items;
    //주문합계
    private Integer order_total_price;
    //배송정보
    private String receiver;
    private String phone;
    private String address;
    //장바구니 주문일때만 사용
    private List<Integer> cartIds;
    private String orderSource; //DIRECT / CART : 주문기능 예외처리시 return할 redirect값
}
