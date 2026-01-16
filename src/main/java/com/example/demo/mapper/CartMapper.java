package com.example.demo.mapper;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartListDto;
import com.example.demo.dto.OrderRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartMapper {
    int deleteCartItemsWhenOrders(List<Integer> cartIds);
    List<OrderRequestDto> selectOrderItemsFromCart(List<Integer> cartIds);
    int insertCart(CartDto dto);
    CartDto selectCartItem(Map<String, Object> map);
    int updateCartQuantity(CartDto dto);
    List<CartListDto> selectCartList(int member_id);
    int countCartItems(int member_id);
    int updateUserCartQuantity(CartListDto dto);
    int deleteCartItem(CartListDto dto);
    List<Integer> selectCartIdsByMember(int member_id);
}
