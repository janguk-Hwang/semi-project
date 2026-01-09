package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class BoardDto {
    private Integer board_id;
    private String title;
    private String content;
    private String board_type;
    private Integer like_count;
    private Integer read_count;
    private Date created_at;
    private Integer member_id;

}
