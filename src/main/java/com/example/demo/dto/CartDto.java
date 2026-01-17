package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDto {
    private Integer cart_id;
    private Integer quantity;
    private Date created_at;
    private Integer member_id;
    private Integer product_id;
    private Integer option_id; //null이 가능해야하기때문에 int가 아닌 Integer타입
}
