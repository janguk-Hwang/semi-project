package com.example.demo.mapper;

import com.example.demo.dto.ProductOptionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductOptionMapper {
    List<ProductOptionDto> selectProductOption(int product_id);
}
