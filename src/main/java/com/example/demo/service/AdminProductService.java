package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final AdminMapper adminMapper;

    public int product_insert(AdminProductDto dto){
        ProductDto productDto=new ProductDto();
        productDto.setProduct_name(dto.getProduct_name());
        productDto.setProduct_type(dto.getProduct_type());
        productDto.setPrice(dto.getPrice());
        productDto.setDescription(dto.getDescription());
        productDto.setStock(dto.getStock());
        productDto.setArtist_id(dto.getArtist_id());
       return adminMapper.product_insert(productDto);
    }
    public int stock_insert(AdminProductDto dto){
        Stock_logDto stockLogDto=new Stock_logDto();
        stockLogDto.setChange_type(dto.getChange_type());
        stockLogDto.setQuantity(dto.getQuantity());
        stockLogDto.setOption_id(dto.getOption_id());
       return adminMapper.stock_insert(stockLogDto);
    }
    public  int product_option_insert(AdminProductDto dto){
        ProductOptionDto productOptionDto=new ProductOptionDto();
        Integer productid=adminMapper.product_select_id(dto.getProduct_name());
        productOptionDto.setOption_name(dto.getOption_name());
        productOptionDto.setProduct_id(productid);
        productOptionDto.setStock(dto.getStock());
        return adminMapper.product_option_insert(productOptionDto);
    }
    public int product_select_id(String product_name){
        return adminMapper.product_select_id(product_name);
    }
    public MusicianDto musician_allselect(){
        return adminMapper.musician_allselect();
    }
}
