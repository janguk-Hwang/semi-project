package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminFileinfoRequestDto {
    private Integer file_id;
    private String originfilename;
    private String savefilename;
    private String ref_type;
    private Integer ref_id;
    private String file_role;
    private List<MultipartFile> file1;
}
