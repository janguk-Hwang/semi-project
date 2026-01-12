package com.example.demo.service;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartListDto;
import com.example.demo.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public List<CartListDto> selectCartList(int member_id){
        List<CartListDto> list=cartMapper.selectCartList(member_id);
        for(CartListDto dto : list){
             dto.setTotal_price(dto.getProduct_price()*dto.getQuantity());
        }
        return list;
    }

    public void addCart(CartDto dto){
        Map<String, Object> map=new HashMap<>();
        map.put("member_id",dto.getMember_id());
        map.put("product_id",dto.getProduct_id());
        map.put("option_id",dto.getOption_id());
        CartDto existItem=cartMapper.selectCartItem(map);

        if(existItem!=null){
            dto.setCart_id(existItem.getCart_id());
            cartMapper.updateCartQuantity(dto);
        }else{
            cartMapper.insertCart(dto);
        }
    }

    public void updateUserCartQuantity(int cart_id, int quantity){
        CartListDto dto=new CartListDto();
        dto.setCart_id(cart_id);
        dto.setQuantity(quantity);

        cartMapper.updateUserCartQuantity(dto);
    }

    public int deleteCartItem(int cart_id, int member_id){
        CartListDto dto=new CartListDto();
        dto.setCart_id(cart_id);
        dto.setMember_id(member_id);

        return cartMapper.deleteCartItem(dto);
    }
}
