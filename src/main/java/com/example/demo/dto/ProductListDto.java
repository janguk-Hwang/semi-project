package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductListDto {
    private Integer product_id;
    private String product_name;
    private Integer price;
    private String savefilename;
    private String product_type;
}
