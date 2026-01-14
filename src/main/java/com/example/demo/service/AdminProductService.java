package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final AdminMapper adminMapper;

    public int product_insert(AdminProductDto dto) {
        Integer sumstock = 0;
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            for (Product_optionDto p : dto.getOptions()) {
                sumstock += p.getStock();
            }
            dto.setStock(sumstock);
        }
        ProductDto productDto = new ProductDto();
        productDto.setProduct_name(dto.getProduct_name());
        productDto.setProduct_type(dto.getProduct_type());
        productDto.setPrice(dto.getPrice());
        productDto.setDescription(dto.getDescription());
        productDto.setStock(dto.getStock());
        productDto.setArtist_id(dto.getArtist_id());
        productDto.setStock(dto.getStock());
        if (dto.getOptions() != null) {
            productDto.setHas_option("Y");
        } else {
            productDto.setHas_option("N");
        }
        return adminMapper.product_insert(productDto);
    }

    public int stock_insert(AdminProductDto dto) {
        Map<String,Object> option_map=new HashMap<>();
        Stock_logDto stockLogDto = new Stock_logDto();
        Integer productid = adminMapper.product_select_id(dto.getProduct_name());
        option_map.put("product_id",productid);
        stockLogDto.setChange_type("STOCK_IN");
        if(dto.getOptions()!=null){
            int stock_insert_sum=0;
            for (Product_optionDto p:dto.getOptions()){
                option_map.put("option_name",p.getOption_name());
                stock_insert_sum+=adminMapper.stock_insert(new Stock_logDto(0,"STOCK_IN",p.getStock(),null,adminMapper.select_option_id(option_map),productid));
            }
            return stock_insert_sum;
        }
          return adminMapper.non_option_stock_log_insert(new Stock_logDto(0,"STOCK_IN",dto.getStock(),null,null,productid));
    }

    public int product_option_insert(AdminProductDto dto) {
        List<Product_optionDto> optionDtos = dto.getOptions();

        ProductOptionDto productOptionDto = new ProductOptionDto();
        Integer productid = adminMapper.product_select_id(dto.getProduct_name());
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            int option_insert=0;
            for (Product_optionDto p : optionDtos) {
                productOptionDto.setOption_name(p.getOption_name());
                productOptionDto.setProduct_id(productid);
                productOptionDto.setStock(p.getStock());
                option_insert+=adminMapper.product_option_insert(productOptionDto);
            }
            return option_insert;
        }else {
            return 0;
        }
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
// 예외처리 해야 할 것 = 수량을 기입하지 않았을 경우,
// 같은 이름의 상품이 있을경우,

//상품관리 페이지 수정 기능 , 삭제 기능 , 이미지변경 추가
