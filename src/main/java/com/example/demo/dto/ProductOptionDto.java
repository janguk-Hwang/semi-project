package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductOptionDto {
    private Integer option_id;
    private String option_name;
    private Integer stock;
    private Integer product_id;
}
