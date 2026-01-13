package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemDto {
    private Integer order_item_id;
    private Integer quantity;
    private Integer price;
    private Integer order_id;
    private Integer product_id;
    private Integer option_id;
}
