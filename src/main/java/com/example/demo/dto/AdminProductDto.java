package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminProductDto {
    private Integer product_id;
    @NotBlank(message = "필수입력사항 입니다.")
    private String product_name;
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;
    @NotBlank(message = "필수입력사항 입니다.")
    private String  description;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String product_type;
    private Date create_at;
    @NotNull(message = "아티스트를 선택해주세요.")
    private Integer artist_id;
    private Integer stock;
    private String has_option;
    private Integer option_id;
    private String option_name;
    private Integer log_id;
    private String change_type;
    private Integer quantity;
    private Date created_at;

    private Integer file_id;
    private String originfilename;
    private String savefilename;
    private String ref_type;
    private Integer ref_id;
    private String file_role;
    private List<MultipartFile> file1;

    private List<Product_optionDto> options;

}
