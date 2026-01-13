package com.example.demo.mapper;

import com.example.demo.dto.OrdersDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {
    int insertOrder(OrdersDto dto);
    int selectCurrentOrderId();

}
