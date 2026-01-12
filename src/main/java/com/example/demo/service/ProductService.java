package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductListDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.Page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    //전체,album,md 카테고리 전용
    public Map<String,Object> allProductList(String product_type,int pageNum){
        int totalRowCount=(product_type==null)?productMapper.countAll():productMapper.countByType(product_type);
        PageInfo pageInfo=new PageInfo(pageNum,9,3,totalRowCount);

        Map<String,Object> map=new HashMap<>();
        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("product_type",product_type);
        List<ProductListDto> list=(product_type==null)?productMapper.allProductList(map):productMapper.selectProductType(map);

        Map<String,Object> result=new HashMap<>();
        result.put("products",list);
        result.put("pageInfo",pageInfo);
        result.put("totalCount",totalRowCount);
        result.put("product_type",product_type);
        return result;
    }

    public Map<String,Object> productListByArtist(int artist_id, int pageNum){
        int totalRowCount=productMapper.countByArtist(artist_id);
        PageInfo pageInfo=new PageInfo(pageNum,9,3,totalRowCount);

        Map<String,Object> map=new HashMap<>();
        map.put("artist_id",artist_id);
        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<ProductListDto> list=productMapper.selectProductByArtist(map);

        Map<String,Object> result=new HashMap<>();
        result.put("products",list);
        result.put("pageInfo",pageInfo);
        result.put("totalCount",totalRowCount);
        result.put("artist_id",artist_id);

        return result;
    }

    public int countAll(){
        return productMapper.countAll();
    }

    public int countByType(String product_type){
        return productMapper.countByType(product_type);
    }
}
