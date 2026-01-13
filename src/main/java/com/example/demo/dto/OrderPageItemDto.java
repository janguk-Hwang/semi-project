package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderPageItemDto {
    private Integer product_id;
    private Integer option_id;
    private String product_name;
    private String option_name;
    private String savefilename;
    private Integer quantity;
    private Integer unit_price;
    private Integer total_price;
}
