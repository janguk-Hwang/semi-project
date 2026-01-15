package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailItemDto { //주문상품 1개 Dto
    private Integer order_item_id;

    private Integer product_id;
    private String product_name;
    private String savefilename;

    private Integer option_id;
    private String option_name;

    private Integer quantity;
    private Integer price; //단가
    private Integer total_price; //price * quantity

    //후기작성 여부 판단용
    private Integer review_count; //0->후기작성 버튼 노출 / 1->작성완료(숨김)
}
