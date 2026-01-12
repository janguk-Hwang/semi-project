package com.example.demo.mapper;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartMapper {
    int insertCart(CartDto dto);
    CartDto selectCartItem(Map<String, Object> map);
    int updateCartQuantity(CartDto dto);
    List<CartListDto> selectCartList(int member_id);
    int countCartItems(int member_id);
    int updateUserCartQuantity(CartListDto dto);
}
