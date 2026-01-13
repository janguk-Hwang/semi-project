package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockUpdateDto {
    private Integer product_id;
    private Integer option_id;
    private Integer quantity;
}
