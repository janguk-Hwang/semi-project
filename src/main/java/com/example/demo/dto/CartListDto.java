package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartListDto {
    //cart table
    private Integer cart_id;
    private Integer quantity;
    //product
    private Integer product_id;
    private String product_name;
    private Integer product_price;
    //option
    private Integer option_id;
    private String option_name;
    //fileinfo
    private String savefilename;
    //계산용 price * quantity
    private Integer total_price;
    //장바구니 개수
    private Integer cartCount;
    private Integer stock;

//    private Integer member_id;
}
