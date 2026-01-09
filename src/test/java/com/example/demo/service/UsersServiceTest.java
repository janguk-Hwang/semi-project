package com.example.demo.service;

import com.example.demo.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MusicianService musicianService;
    @Autowired
    private ProductOptionService productOptionService;
    @Autowired
    private CartService cartService;
    @Test
    public void selectList(){
        List<CartListDto> list=cartService.selectCartList(61);
        list.forEach(l->{
            System.out.println(l);
        });
    }
}
