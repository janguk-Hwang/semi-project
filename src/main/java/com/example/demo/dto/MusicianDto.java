package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MusicianDto {
    private Integer artist_id;
    private String artist_name;
    private String profile;
    private String isteam;
    private Integer member_id;
}
