package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDto {
    private Integer review_id;
    private Integer rating;
    private String content;
    private Date created_at;
    private Integer product_id;
    private Integer member_id;
    private Integer order_id;
    private Integer option_id;
}
