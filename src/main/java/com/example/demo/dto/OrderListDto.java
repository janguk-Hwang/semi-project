package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderListDto { //주문내역 조회(요약) 화면용 DTO
    private Integer order_id; //주문번호
    private Date created_at; //주문일자
    private String represent_product_name; //대표 상품명
    private Integer item_count; //주문 상품 개수
    private Integer total_price; //주문 총금액
    private String order_status; //주문상태(ORDERED,SHIPPING,DELIVERED,CANCELLED)
    private String confirmed; //구매확정여부(Y/N)
}
