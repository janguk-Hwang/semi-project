package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {
    private Integer product_id;
    private String product_name;
    private Integer price;
    private String  description;
    private String product_type;
    private Date create_at;
    private Integer artist_id;
    private Integer stock;
    private String has_option;
}
