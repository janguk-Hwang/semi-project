package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminProductDto {
    private Integer product_id;
    private String product_name;
    private Integer price;
    private String  description;
    private String product_type;
    private Date create_at;
    private Integer artist_id;
    private Integer stock;
    private String has_option;
    private Integer option_id;
    private String option_name;
    private Integer log_id;
    private String change_type;
    private Integer quantity;
    private Date created_at;

}
