package com.example.demo.mapper;

import com.example.demo.dto.CartDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {
    int insertCart(CartDto dto);
}
