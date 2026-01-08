package com.example.demo.mapper;

import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    ProductDetailDto selectProduct(int product_id);
    List<ProductListDto> allProductList(Map<String,Object> map);
    List<ProductListDto> selectProductType(Map<String,Object> map);
    List<ProductListDto> selectProductByArtist(Map<String,Object> map);
    int countAll();
    int countByType(String product_type);
    int countByArtist(int artist_id);
}
