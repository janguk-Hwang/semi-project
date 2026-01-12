package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class BoardDto {
    private int board_id;
    private String title;
    private String content;
    private String board_type;
    private int like_count;
    private int read_count;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created_at;
    private int member_id;

    //users 멤버 아이디 참조

}
