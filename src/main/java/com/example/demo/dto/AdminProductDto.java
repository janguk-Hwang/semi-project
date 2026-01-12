package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminProductDto {
    private Integer product_id;
    @NotBlank(message = "필수입력사항 입니다.")
    private String product_name;
    @NotBlank(message = "필수입력사항 입니다.")
    private Integer price;
    @NotBlank(message = "필수입력사항 입니다.")
    private String  description;
    private String product_type;
    private Date create_at;
    private Integer artist_id;
    private Integer stock;
    private Integer option_id;
    private String option_name;
    private Integer log_id;
    private String change_type;
    private Integer quantity;
    private Date created_at;

}
