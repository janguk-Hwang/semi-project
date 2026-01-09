package com.example.demo.service;

import com.example.demo.dto.FileinfoDto;
import com.example.demo.dto.ProductDetailDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductOptionDto;
import com.example.demo.mapper.FileInfoMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ProductOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionMapper productOptionMapper;
    private final ProductMapper productMapper;
    private final FileInfoMapper fileInfoMapper;

    public Map<String, Object> getProductDetail(int product_id){
        ProductDetailDto product=productMapper.selectProduct(product_id);
        List<ProductOptionDto> options=productOptionMapper.selectProductOption(product_id);
        List<FileinfoDto> detailImages=fileInfoMapper.selectProductDetailImg(product_id);

        Map<String,Object> result=new HashMap<>();
        result.put("product",product);
        result.put("options",options);
        result.put("detailImages",detailImages);

        return result;
    }

    public List<ProductOptionDto> optionList(int product_id){
        return productOptionMapper.selectProductOption(product_id);
    }
}
