package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CommentsDto {
    private String comment_id;
    private String content;
    private Date create_at;
    private int board_id;
    private int member_id;
}
