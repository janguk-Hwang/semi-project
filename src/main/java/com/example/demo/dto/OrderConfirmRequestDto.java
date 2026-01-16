package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderConfirmRequestDto { // 단일/여러상품 통합 주문요청용
    private List<OrderRequestDto> items;
    private String address;
    private List<Integer> cartIds;
    private String orderSource; //DIRECT / CART : 주문기능 예외처리시 return할 redirect값
}
