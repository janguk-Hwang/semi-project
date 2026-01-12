package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Stock_logDto {
    private Integer log_id;
    private String change_type;
    private Integer quantity;
    private Date created_at;
    private Integer option_id;
}
