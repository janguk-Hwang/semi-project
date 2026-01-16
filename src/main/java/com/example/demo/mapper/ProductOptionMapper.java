package com.example.demo.mapper;

import com.example.demo.dto.ProductOptionDto;
import com.example.demo.dto.StockUpdateDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductOptionMapper {
    int increaseOptionStock(StockUpdateDto dto);
    List<ProductOptionDto> selectProductOption(int product_id);
    int selectOptionStock(int option_id);
    ProductOptionDto selectOptionById(int option_id);
}
