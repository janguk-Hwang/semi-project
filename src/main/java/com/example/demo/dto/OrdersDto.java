package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdersDto { //orders 테이블 DB용 DTO
    private Integer order_id;
    private String order_status;
    private Integer total_price;
    private String address;
    private Date created_at;
    private Integer member_id;
}
