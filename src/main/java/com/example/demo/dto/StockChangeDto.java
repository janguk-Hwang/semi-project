package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockChangeDto {
    private Integer product_id;
    private Integer option_id;
    private String changetype;
    private Integer quantity;
}
