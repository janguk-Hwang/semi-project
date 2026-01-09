package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetailDto {
    private Integer product_id;
    private String product_name;
    private Integer price;
    private String description;
    private Integer stock;
    private String has_option;
    private String savefilename;
}
