package com.example.demo.mapper;

import com.example.demo.dto.OrderItemDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper {
    int insertOrder(OrderItemDto dto);

}
