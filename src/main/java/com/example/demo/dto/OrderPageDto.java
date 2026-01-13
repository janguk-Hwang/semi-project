package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderPageDto {
    //상품정보
    private Integer product_id;
    private Integer option_id;
    private String product_name;
    private String option_name;
    private String savefilename;
    private Integer quantity;
    private Integer unit_price;
    private Integer total_price;
    //주문합계
    private Integer order_total_price;
    //배송정보
    private String receiver;
    private String phone;
    private String address;
}
