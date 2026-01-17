package com.example.demo.service;

import com.example.demo.dto.CartDto;
import com.example.demo.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public void insertCart(CartDto dto){
        cartMapper.insertCart(dto);
    }
}
