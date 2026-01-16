package com.example.demo.mapper;

import com.example.demo.dto.OrderItemDto;
import com.example.demo.dto.OrderPageItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    List<OrderItemDto> selectOrderItemsByOrderId(int order_id);
    int insertOrder(OrderItemDto dto);
    //선택 장바구니 상품 주문용
    List<OrderPageItemDto> selectOrderItemsByCart(@Param("member_id") int member_id,
                                                  @Param("cartIds") List<Integer> cartIds);
    //전체 장바구니 상품 주문용
    List<OrderPageItemDto> selectOrderItemByMember(@Param("member_id") int member_id);
}
