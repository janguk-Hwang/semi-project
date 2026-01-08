package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Musician_memberDto {
    private Integer membernum;
    private String member_name;
    private Integer artist_id;
}
