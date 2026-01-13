package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequestDto { //controller -> service 전달용(주문요청용)
    private int product_id;
    private Integer option_id;
    private int quantity;
    private int price;
    private String address;
}
