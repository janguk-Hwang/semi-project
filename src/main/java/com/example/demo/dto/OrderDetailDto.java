package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailDto { //주문 전체정보
    private Integer order_id;
    private Date created_at;
    private String order_status;
    private String confirmed; // Y/N
    private Integer total_price;
    private String address;
    private String phone;
    //주문 상품 목록
    private List<OrderDetailItemDto> items;
}
