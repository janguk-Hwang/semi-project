package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileinfoDto {
    private Integer file_id;
    private String originfilename;
    private String savefilename;
    private String ref_type;
    private Integer ref_id;
}
