package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBoardRequestDto {
    private Integer board_id;
    private String title;
    private String content;
    private String board_type;
    private Integer like_count;
    private Integer read_count;
    private Date created_at;
    private Integer member_id;
    private String id;
}
