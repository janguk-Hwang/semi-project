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
        Integer sumstock=0;
        if(dto.getOptions()!=null && !dto.getOptions().isEmpty()){
            for (Product_optionDto p:dto.getOptions()){
                sumstock += p.getStock();
            }
            dto.setStock(sumstock);
        }
        ProductDto productDto=new ProductDto();
        productDto.setProduct_name(dto.getProduct_name());
        productDto.setProduct_type(dto.getProduct_type());
        productDto.setPrice(dto.getPrice());
        productDto.setDescription(dto.getDescription());
        productDto.setStock(dto.getStock());
        productDto.setArtist_id(dto.getArtist_id());
        productDto.setStock(dto.getStock());
        if (dto.getOption_name()!=null){
            productDto.setHas_option("Y");
        }else {
            productDto.setHas_option("N");
        }
       return adminMapper.product_insert(productDto);
    }
    public int stock_insert(AdminProductDto dto){
        int optionid= adminMapper.select_option_id(adminMapper.product_select_id(dto.getProduct_name()));
        dto.setOption_id(optionid);
        Stock_logDto stockLogDto=new Stock_logDto();
        stockLogDto.setChange_type(dto.getChange_type());
        stockLogDto.setQuantity(dto.getStock());
        stockLogDto.setOption_id(optionid);
       return adminMapper.stock_insert(stockLogDto);
    }
    public  int product_option_insert(AdminProductDto dto){

        ProductOptionDto productOptionDto=new ProductOptionDto();
        Integer productid=adminMapper.product_select_id(dto.getProduct_name());
        productOptionDto.setOption_name(dto.getOption_name());
        productOptionDto.setProduct_id(productid);
        productOptionDto.setStock(dto.getStock());
        if(dto.getOptions()!=null && !dto.getOptions().isEmpty()){
            return adminMapper.product_option_insert(productOptionDto);
        }else{
            return 0;
        }
        // insert 상품관리
    }
    public int product_select_id(String product_name){
        return adminMapper.product_select_id(product_name);
    }
    public int insert_img(AdminFileinfoRequestDto dto){
        return adminMapper.insert_img(dto);
    }
    public MusicianDto musician_allselect(){
        return adminMapper.musician_allselect();
    }
}
